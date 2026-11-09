package com.example.domain.model

/**
 * Represents the real-time synchronization state between
 * the local Room Database and the remote cloud sync service.
 */
sealed interface SyncStatus {
    /**
     * Data is fully synchronized with the cloud.
     */
    data class Synced(
        val lastSyncedEpoch: Long = System.currentTimeMillis(),
        val syncedEntitiesCount: Int = 0
    ) : SyncStatus

    /**
     * Synchronization is currently in progress.
     */
    data class Syncing(
        val stageDescription: String = "جارٍ مزامنة بيانات الأحياء...",
        val progressFraction: Float? = null
    ) : SyncStatus

    /**
     * Device is disconnected from internet. All changes are securely persisted in Room.
     */
    data class Offline(
        val pendingChangesCount: Int = 0,
        val reason: String = "بدون اتصال • التخزين المحلي في Room نشط"
    ) : SyncStatus

    /**
     * Sync encountered an error or network timeout.
     * Incorporates exponential backoff metadata for transient failure handling.
     */
    data class Error(
        val errorMessage: String = "تعذر إتمام المزامنة",
        val canRetry: Boolean = true,
        val retryAttempt: Int = 0,
        val nextRetryDelaySeconds: Long? = null,
        val isBackingOff: Boolean = false
    ) : SyncStatus
}

/**
 * Summary of records persisted in local Room Database.
 */
data class RoomSyncSummary(
    val totalFlashcards: Int = 0,
    val savedDefinitionsCount: Int = 0,
    val examAttemptsCount: Int = 0,
    val keyConceptsCount: Int = 0,
    val totalStudyMinutes: Int = 0,
    val totalXp: Int = 0,
    val lastSyncTimestamp: Long = System.currentTimeMillis(),
    val databaseVersion: Int = 4,
    val isOnline: Boolean = true
)
