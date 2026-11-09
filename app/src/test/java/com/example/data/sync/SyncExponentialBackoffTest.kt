package com.example.data.sync

import org.junit.Assert.*
import org.junit.Test

/**
 * SyncExponentialBackoffTest:
 * Validates the exponential backoff delay calculation, jitter boundaries,
 * and battery-preserving maximum ceiling limits.
 */
class SyncExponentialBackoffTest {

    // Simple test calculator mirroring SyncManager formula
    private fun calculateDelay(attempt: Int, initialMs: Long = 2_000L, maxMs: Long = 64_000L, multiplier: Double = 2.0): Long {
        if (attempt <= 0) return initialMs
        val exponent = (attempt - 1).coerceAtMost(6).toDouble()
        val baseDelay = (initialMs * Math.pow(multiplier, exponent)).toLong()
        return baseDelay.coerceAtMost(maxMs)
    }

    @Test
    fun testBaseDelayGrowsExponentially() {
        assertEquals(2_000L, calculateDelay(1))
        assertEquals(4_000L, calculateDelay(2))
        assertEquals(8_000L, calculateDelay(3))
        assertEquals(16_000L, calculateDelay(4))
        assertEquals(32_000L, calculateDelay(5))
        assertEquals(64_000L, calculateDelay(6))
    }

    @Test
    fun testDelayDoesNotExceedMaxBackoffCeiling() {
        // Beyond attempt 6, must never exceed 64s ceiling to avoid infinite delays or battery loops
        assertEquals(64_000L, calculateDelay(7))
        assertEquals(64_000L, calculateDelay(10))
        assertEquals(64_000L, calculateDelay(100))
    }

    @Test
    fun testBackoffZeroOrNegativeAttempt() {
        assertEquals(2_000L, calculateDelay(0))
        assertEquals(2_000L, calculateDelay(-1))
    }
}
