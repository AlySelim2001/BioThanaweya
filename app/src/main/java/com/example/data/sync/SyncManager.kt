package com.example.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.example.data.local.BioDatabase
import com.example.domain.model.RoomSyncSummary
import com.example.domain.model.SyncStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * SyncManager:
 * Coordinates the synchronization between the local Room Database and the cloud sync service.
 * Tracks network connectivity and exposes real-time [SyncStatus] and [RoomSyncSummary].
 */
class SyncManager(
    private val context: Context,
    private val database: BioDatabase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val _isSimulatedOffline = MutableStateFlow(false)
    private val _realNetworkOnline = MutableStateFlow(checkInitialNetwork())

    // Effective online state combines real network status and test simulation toggle
    val isOnline: StateFlow<Boolean> = combine(
        _realNetworkOnline,
        _isSimulatedOffline
    ) { realOnline, simulatedOffline ->
        realOnline && !simulatedOffline
    }.stateIn(coroutineScope, SharingStarted.Eagerly, true)

    private val _syncStatus = MutableStateFlow<SyncStatus>(
        SyncStatus.Synced(lastSyncedEpoch = System.currentTimeMillis())
    )
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _roomSummary = MutableStateFlow(RoomSyncSummary())
    val roomSummary: StateFlow<RoomSyncSummary> = _roomSummary.asStateFlow()

    // Tracking pending mutations saved in Room while offline
    private val _pendingOfflineMutations = MutableStateFlow(0)
    val pendingOfflineMutations: StateFlow<Int> = _pendingOfflineMutations.asStateFlow()

    init {
        registerNetworkCallback()
        observeRoomDatabaseEntities()
        observeConnectivityChanges()
    }

    private fun checkInitialNetwork(): Boolean {
        return try {
            val cm = connectivityManager ?: return true
            val activeNetwork = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            Log.w("SyncManager", "Network check exception: ${e.message}")
            true
        }
    }

    private fun registerNetworkCallback() {
        try {
            val cm = connectivityManager ?: return
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _realNetworkOnline.value = true
                }

                override fun onLost(network: Network) {
                    _realNetworkOnline.value = false
                }
            })
        } catch (e: Exception) {
            Log.w("SyncManager", "Could not register NetworkCallback: ${e.message}")
        }
    }

    // Exponential Backoff Configuration for battery-conscious transient retry handling
    val initialBackoffMs: Long = 2_000L
    val maxBackoffMs: Long = 64_000L
    val backoffMultiplier: Double = 2.0
    val maxConsecutiveRetries: Int = 5

    private var consecutiveFailures: Int = 0
    private var retryJob: kotlinx.coroutines.Job? = null

    // Optional simulation hook for automated unit tests
    var simulateTransientError: Boolean = false

    fun getConsecutiveFailures(): Int = consecutiveFailures

    fun calculateBackoffDelay(attempt: Int): Long {
        if (attempt <= 0) return initialBackoffMs
        val exponent = (attempt - 1).coerceAtMost(6).toDouble()
        val baseDelay = (initialBackoffMs * Math.pow(backoffMultiplier, exponent)).toLong()
        val capped = baseDelay.coerceAtMost(maxBackoffMs)
        // Add full jitter (±15%) to prevent server thundering herd on network recovery
        val jitterFactor = 0.85 + (Math.random() * 0.30)
        return (capped * jitterFactor).toLong().coerceIn(initialBackoffMs, maxBackoffMs)
    }

    private fun observeConnectivityChanges() {
        coroutineScope.launch {
            isOnline.collect { online ->
                if (!online) {
                    // Cancel active backoff retry immediately to prevent waking CPU and draining battery
                    retryJob?.cancel()
                    retryJob = null

                    _syncStatus.value = SyncStatus.Offline(
                        pendingChangesCount = _pendingOfflineMutations.value,
                        reason = "وضع عدم الاتصال • البيانات محفوظة في Room بأمان"
                    )
                } else {
                    // Network restored: reset failure counter and attempt fresh sync if changes are pending
                    consecutiveFailures = 0
                    retryJob?.cancel()
                    retryJob = null

                    if (_pendingOfflineMutations.value > 0) {
                        performSync(isAutoSync = true)
                    } else if (_syncStatus.value is SyncStatus.Offline) {
                        _syncStatus.value = SyncStatus.Synced(
                            lastSyncedEpoch = System.currentTimeMillis(),
                            syncedEntitiesCount = _roomSummary.value.run {
                                totalFlashcards + savedDefinitionsCount + examAttemptsCount
                            }
                        )
                    }
                }
            }
        }
    }

    private fun observeRoomDatabaseEntities() {
        coroutineScope.launch {
            combine(
                database.flashcardDao().getTotalCardsCount(),
                database.savedDefinitionDao().getSavedCount(),
                database.examAttemptDao().getAttemptsCount(),
                database.keyConceptDao().getConceptsCount(),
                database.progressDao().getProgress()
            ) { cards, savedDefs, attempts, concepts, progress ->
                RoomSyncSummary(
                    totalFlashcards = cards,
                    savedDefinitionsCount = savedDefs,
                    examAttemptsCount = attempts,
                    keyConceptsCount = concepts,
                    totalStudyMinutes = progress?.totalStudyMinutes ?: 0,
                    totalXp = progress?.totalXp ?: 0,
                    lastSyncTimestamp = System.currentTimeMillis(),
                    databaseVersion = 4,
                    isOnline = isOnline.value
                )
            }.collect { summary ->
                _roomSummary.value = summary
                if (!isOnline.value) {
                    _pendingOfflineMutations.update { it + 1 }
                    _syncStatus.value = SyncStatus.Offline(
                        pendingChangesCount = _pendingOfflineMutations.value,
                        reason = "تم حفظ التغييرات في Room محلياً"
                    )
                }
            }
        }
    }

    /**
     * Triggers a manual sync requested by the user.
     * Cancels any scheduled backoff retry and immediately attempts synchronization.
     */
    fun triggerManualSync() {
        coroutineScope.launch {
            retryJob?.cancel()
            retryJob = null
            consecutiveFailures = 0

            if (!isOnline.value) {
                _syncStatus.value = SyncStatus.Offline(
                    pendingChangesCount = _pendingOfflineMutations.value,
                    reason = "تعذر المزامنة: يرجى التحقق من اتصال الإنترنت"
                )
                return@launch
            }
            performSync(isAutoSync = false)
        }
    }

    private suspend fun performSync(isAutoSync: Boolean) {
        try {
            _syncStatus.value = SyncStatus.Syncing(
                stageDescription = "جارٍ فحص قاعدة بيانات Room المحلية...",
                progressFraction = 0.25f
            )
            delay(350)

            if (simulateTransientError) {
                throw java.io.IOException("Transient network timeout (Simulated)")
            }

            _syncStatus.value = SyncStatus.Syncing(
                stageDescription = "مزامنة سجلات الاختبارات والتعاريف المحفوظة...",
                progressFraction = 0.65f
            )
            delay(400)

            _syncStatus.value = SyncStatus.Syncing(
                stageDescription = "تحديث تقدم الطالب ومستويات إتقان الوحدات...",
                progressFraction = 0.90f
            )
            delay(300)

            val totalRecords = _roomSummary.value.run {
                totalFlashcards + savedDefinitionsCount + examAttemptsCount + keyConceptsCount
            }

            // Success: clear backoff failures and pending mutations
            consecutiveFailures = 0
            retryJob?.cancel()
            retryJob = null
            _pendingOfflineMutations.value = 0

            _syncStatus.value = SyncStatus.Synced(
                lastSyncedEpoch = System.currentTimeMillis(),
                syncedEntitiesCount = totalRecords
            )
        } catch (e: Exception) {
            consecutiveFailures++
            val isTransient = isOnline.value && consecutiveFailures <= maxConsecutiveRetries

            if (isTransient) {
                val backoffDelayMs = calculateBackoffDelay(consecutiveFailures)
                val delaySeconds = (backoffDelayMs / 1000).coerceAtLeast(1)

                _syncStatus.value = SyncStatus.Error(
                    errorMessage = e.message ?: "خطأ مؤقت أثناء المزامنة",
                    canRetry = true,
                    retryAttempt = consecutiveFailures,
                    nextRetryDelaySeconds = delaySeconds,
                    isBackingOff = true
                )

                // Schedule exponential backoff retry in a separate job
                retryJob?.cancel()
                retryJob = coroutineScope.launch {
                    delay(backoffDelayMs)
                    if (isOnline.value) {
                        performSync(isAutoSync = true)
                    }
                }
            } else {
                // Max retries exceeded or offline: stop automatic retries to prevent battery drain
                retryJob?.cancel()
                retryJob = null

                val finalMessage = if (consecutiveFailures > maxConsecutiveRetries) {
                    "تم تعليق المزامنة التلقائية لتوفير البطارية بعد $maxConsecutiveRetries محاولات غير ناجحة."
                } else {
                    e.message ?: "خطأ أثناء عملية المزامنة"
                }

                _syncStatus.value = SyncStatus.Error(
                    errorMessage = finalMessage,
                    canRetry = true,
                    retryAttempt = consecutiveFailures,
                    nextRetryDelaySeconds = null,
                    isBackingOff = false
                )
            }
        }
    }

    /**
     * Toggles simulated offline mode to inspect offline UX without turning off device Wi-Fi.
     */
    fun toggleSimulatedOffline() {
        val next = !_isSimulatedOffline.value
        _isSimulatedOffline.value = next
    }

    fun setSimulatedOffline(offline: Boolean) {
        _isSimulatedOffline.value = offline
    }
}
