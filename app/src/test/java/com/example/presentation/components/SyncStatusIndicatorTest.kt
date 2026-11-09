package com.example.presentation.components

import com.example.domain.model.RoomSyncSummary
import com.example.domain.model.SyncStatus
import org.junit.Assert.*
import org.junit.Test

/**
 * SyncStatusIndicatorTest:
 * Unit tests validating state modeling, status transitions, and data integrity
 * for Room Database and Background Sync service visualization.
 */
class SyncStatusIndicatorTest {

    @Test
    fun testSyncedStateProperties() {
        val now = System.currentTimeMillis()
        val syncedState: SyncStatus = SyncStatus.Synced(lastSyncedEpoch = now, syncedEntitiesCount = 42)

        assertTrue(syncedState is SyncStatus.Synced)
        assertEquals(now, (syncedState as SyncStatus.Synced).lastSyncedEpoch)
        assertEquals(42, syncedState.syncedEntitiesCount)
    }

    @Test
    fun testSyncingStateProperties() {
        val syncingState = SyncStatus.Syncing(
            stageDescription = "جارٍ مزامنة تعاريف الأحياء وسجلات الامتحانات...",
            progressFraction = 0.65f
        )

        assertEquals("جارٍ مزامنة تعاريف الأحياء وسجلات الامتحانات...", syncingState.stageDescription)
        assertEquals(0.65f, syncingState.progressFraction ?: 0f, 0.001f)
    }

    @Test
    fun testOfflineStateProperties() {
        val offlineState = SyncStatus.Offline(
            pendingChangesCount = 3,
            reason = "تم حفظ التغييرات محلياً في Room"
        )

        assertEquals(3, offlineState.pendingChangesCount)
        assertEquals("تم حفظ التغييرات محلياً في Room", offlineState.reason)
    }

    @Test
    fun testErrorStateRetryability() {
        val errorState = SyncStatus.Error(
            errorMessage = "انتهت مهلة الاتصال بالخادم",
            canRetry = true,
            retryAttempt = 2,
            nextRetryDelaySeconds = 8,
            isBackingOff = true
        )

        assertTrue(errorState.canRetry)
        assertTrue(errorState.isBackingOff)
        assertEquals(2, errorState.retryAttempt)
        assertEquals(8L, errorState.nextRetryDelaySeconds)
        assertEquals("انتهت مهلة الاتصال بالخادم", errorState.errorMessage)
    }

    @Test
    fun testRoomSyncSummaryIntegrity() {
        val summary = RoomSyncSummary(
            totalFlashcards = 150,
            savedDefinitionsCount = 28,
            examAttemptsCount = 5,
            keyConceptsCount = 6,
            totalStudyMinutes = 90,
            totalXp = 1450,
            databaseVersion = 4,
            isOnline = true
        )

        assertEquals(150, summary.totalFlashcards)
        assertEquals(28, summary.savedDefinitionsCount)
        assertEquals(5, summary.examAttemptsCount)
        assertEquals(6, summary.keyConceptsCount)
        assertEquals(90, summary.totalStudyMinutes)
        assertEquals(1450, summary.totalXp)
        assertEquals(4, summary.databaseVersion)
        assertTrue(summary.isOnline)

        val totalRecords = summary.totalFlashcards +
                summary.savedDefinitionsCount +
                summary.examAttemptsCount +
                summary.keyConceptsCount
        assertEquals(189, totalRecords)
    }

    @Test
    fun testStateTransitionFlow() {
        var currentStatus: SyncStatus = SyncStatus.Synced()
        assertTrue(currentStatus is SyncStatus.Synced)

        // Offline event occurs
        currentStatus = SyncStatus.Offline(pendingChangesCount = 1)
        val offline = currentStatus as? SyncStatus.Offline
        assertNotNull(offline)
        assertEquals(1, offline?.pendingChangesCount)

        // Sync triggered
        currentStatus = SyncStatus.Syncing(progressFraction = 0.5f)
        val syncing = currentStatus as? SyncStatus.Syncing
        assertNotNull(syncing)

        // Completed
        currentStatus = SyncStatus.Synced(syncedEntitiesCount = 10)
        val synced = currentStatus as? SyncStatus.Synced
        assertNotNull(synced)
        assertEquals(10, synced?.syncedEntitiesCount)
    }
}
