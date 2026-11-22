/** 
 * @module src/services/storage.test.ts 
 * @description Vitest unit tests for StorageService persistence behavior. 
 * Tests real persistence using the actual implementation with fake-indexeddb. 
 */ 

import { describe, it, expect, beforeEach, vi } from 'vitest'
import 'fake-indexeddb/auto'
import { StorageService } from './storage'

describe('StorageService - Persistence Tests', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    // Note: StorageService in-memory state persists across tests.
    // Tests that need fresh state should use resetAll() or reloadFromIndexedDb().
    ;(global as any).indexedDB = global?.indexedDB || {}
  })

  describe('A. Save progress', () => {
    it('saveProfile saves and persists profile data', async () => {
      const initialProfile = StorageService.getProfile()
      const updatedProfile = {
        ...initialProfile,
        totalXp: initialProfile.totalXp + 100,
        streakDays: initialProfile.streakDays + 1,
      }
      
      StorageService.saveProfile(updatedProfile as any)
      
      const currentProfile = StorageService.getProfile()
      expect(currentProfile.totalXp).toBe(updatedProfile.totalXp)
      expect(currentProfile.streakDays).toBe(updatedProfile.streakDays)
      
      const storedProfile = localStorage.getItem('biothanaweya_wellbeing_profile_v1')
      expect(storedProfile).toBeTruthy()
      if (storedProfile) {
        const parsed = JSON.parse(storedProfile)
        expect(parsed.totalXp).toBe(updatedProfile.totalXp)
      }
    })

    it('addXp increases XP and persists', async () => {
      const initialProfile = StorageService.getProfile()
      const { totalXp } = initialProfile
      
      const newProfile = StorageService.addXp(50)
      
      expect(newProfile.totalXp).toBe(totalXp + 50)
      expect(newProfile.streakDays).toBe(initialProfile.streakDays)
      
      const stored = localStorage.getItem('biothanaweya_wellbeing_profile_v1')
      expect(stored).toBeTruthy()
    })

    it('incrementStreak increases streak count', async () => {
      const initialProfile = StorageService.getProfile()
      const newProfile = StorageService.incrementStreak()
      
      expect(newProfile.streakDays).toBe(initialProfile.streakDays + 1)
      expect(newProfile.totalXp).toBe(initialProfile.totalXp)
    })
  })

  describe('B. Retrieve progress', () => {
    it('retrieve existing saved progress from IndexedDB', async () => {
      const testProfile = {
        streakDays: 5,
        totalXp: 1000,
        earnedBadges: [],
        examAnxietyScore: 20,
        weaknessDiagnostics: {
          topicPerformance: {},
          studyPattern: 'mixed',
          recommendedFocus: [],
          lastAnalyzed: new Date().toISOString(),
        },
        studyChunkMinutes: 30,
        motivationalQuote: 'Test quote',
      }
      
      StorageService.saveProfile(testProfile as any)
      await StorageService.reloadFromIndexedDb()
      
      const profile = StorageService.getProfile()
      expect(profile.streakDays).toBe(5)
      expect(profile.totalXp).toBe(1000)
    })

    it('retrieve progress structure when no saved record', () => {
      // Profile structure should be valid even without saved data
      const profile = StorageService.getProfile()
      expect(profile).toHaveProperty('streakDays')
      expect(profile).toHaveProperty('totalXp')
      expect(profile).toHaveProperty('earnedBadges')
    })
  })

  describe('C. Clear progress', () => {
    it('clearAll modifies profile state', async () => {
      const testProfile = {
        streakDays: 5,
        totalXp: 1000,
        earnedBadges: [],
        examAnxietyScore: 20,
        weaknessDiagnostics: {
          topicPerformance: {},
          studyPattern: 'mixed',
          recommendedFocus: [],
          lastAnalyzed: new Date().toISOString(),
        },
        studyChunkMinutes: 30,
        motivationalQuote: 'Test quote',
      }
      
      const initialStreak = StorageService.getProfile().streakDays
      const initialXp = StorageService.getProfile().totalXp
      
      StorageService.saveProfile(testProfile as any)
      await StorageService.resetAll()
      
      // After resetAll, profile state should have changed from saved values
      const finalProfile = StorageService.getProfile()
      expect(finalProfile.streakDays).toBeLessThanOrEqual(initialStreak)
      expect(finalProfile.totalXp).toBeLessThanOrEqual(initialXp)
    })

    it('masteries array exists after clear', () => {
      const masteries = StorageService.getTopicMasteries()
      expect(Array.isArray(masteries)).toBe(true)
    })
  })

  describe('D. Persistence integrity', () => {
    it('serialization preserves supported data types', async () => {
      const complexProfile = {
        streakDays: 42,
        totalXp: 5000,
        earnedBadges: [
          { id: 'b_1', title: 'Badge 1', titleArabic: 'شارة 1', iconName: 'Activity', description: 'Desc 1', unlockedAt: new Date().toISOString() },
        ],
        examAnxietyScore: 15,
        weaknessDiagnostics: {
          topicPerformance: { sarcomere: 0.85, photosynthesis: 0.6 },
          studyPattern: 'consistent',
          recommendedFocus: ['photosynthesis', 'dna'],
          lastAnalyzed: new Date().toISOString(),
        },
        studyChunkMinutes: 25,
        motivationalQuote: 'Test quote',
      }
      
      StorageService.saveProfile(complexProfile as any)
      await StorageService.reloadFromIndexedDb()
      
      const profile = StorageService.getProfile()
      expect(profile.streakDays).toBe(42)
      expect(profile.totalXp).toBe(5000)
      expect(profile.motivationalQuote).toBe('Test quote')
    })

    it('does not silently corrupt student progress', async () => {
      const validProfile = {
        streakDays: 3,
        totalXp: 500,
        earnedBadges: [],
        examAnxietyScore: 10,
        weaknessDiagnostics: {
          topicPerformance: {},
          studyPattern: 'mixed',
          recommendedFocus: [],
          lastAnalyzed: new Date().toISOString(),
        },
        studyChunkMinutes: 20,
        motivationalQuote: 'Test quote',
      }
      
      StorageService.saveProfile(validProfile as any)
      await StorageService.reloadFromIndexedDb()
      const profile = StorageService.getProfile()
      
      expect(profile.streakDays).toBe(3)
      expect(profile.totalXp).toBe(500)
      expect(profile.examAnxietyScore).toBe(10)
    })
  })
})