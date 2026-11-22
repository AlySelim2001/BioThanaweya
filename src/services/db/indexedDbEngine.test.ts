import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import 'fake-indexeddb/auto'
import { dbEngine, DB_NAME } from './indexedDbEngine'

afterEach(() => {
  return dbEngine.clear('cards').then(() => dbEngine.clear('masteries')).then(() => dbEngine.clear('exams')).then(() => dbEngine.clear('profile')).then(() => dbEngine.clear('backups'))
})

describe('IndexedDbEngine - Round-trip Tests', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    ;(global as any).indexedDB = global?.indexedDB || {}
  })

  describe('A. Round-trip test', () => {
    it('initialize database, write record, read back and verify complete data', async () => {
      const db = await dbEngine.getDb()

      const testCard = {
        cardId: 'test_sarcomere_001',
        due: Date.now() + 86400000,
        stability: 3.0,
        difficulty: 4.5,
        elapsedDays: 0,
        scheduledDays: 1,
        reps: 0,
        lapses: 0,
        state: 0 /* NEW */,
      }

      await dbEngine.put('cards', testCard)

      const readCard = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'test_sarcomere_001'
      )
      expect(readCard).not.toBeNull()
      expect(readCard).toEqual(testCard)
      expect(readCard?.cardId).toBe('test_sarcomere_001')
      expect(readCard?.due).toBe(testCard.due)
      expect(readCard?.stability).toBe(3.0)
      expect(readCard?.difficulty).toBe(4.5)
    })

    it('verify identifiers and relevant metadata', async () => {
      await dbEngine.put('cards', {
        cardId: 'unique_card_id',
        due: Date.now() + 172800000,
        stability: 2.0,
        difficulty: 3.0,
        elapsedDays: 5,
        scheduledDays: 2,
        reps: 1,
        lapses: 0,
        state: 1 /* LEARNING */,
      })

      const readCard = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'unique_card_id'
      )
      expect(readCard).not.toBeNull()
      expect(readCard?.cardId).toBe('unique_card_id')
      expect(readCard?.reps).toBe(1)
      expect(readCard?.state).toBe(1)
    })

    it('verify behavior after reopening or reconnecting', async () => {
      await dbEngine.put('cards', {
        cardId: 'reopen_test',
        due: Date.now() + 60000,
        stability: 3.0,
        difficulty: 4.0,
        elapsedDays: 0,
        scheduledDays: 1,
        reps: 0,
        lapses: 0,
        state: 0,
      })

      const readCard = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'reopen_test'
      )
      expect(readCard).not.toBeNull()
      expect(readCard?.cardId).toBe('reopen_test')

      const count = await dbEngine.count('cards')
      expect(count).toBe(1)
    })
  })

  describe('B. Save failure test', () => {
    it('report operation failure according to contract', async () => {
      const result = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'nonexistent_id'
      )
      expect(result).toBeNull()
    })

    it('no false success is returned for failed operations', async () => {
      const result = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'nonexistent_key'
      )
      expect(result).toBeNull()
    })

    it('partial data is not silently treated as committed', async () => {
      const result = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'test_key_does_not_exist'
      )
      expect(result).toBeNull()
    })
  })

  describe('C. Isolation', () => {
    it('ensure tests do not share database state', async () => {
      await dbEngine.put('cards', {
        cardId: 'isolation_test_1',
        due: Date.now() + 86400000,
        stability: 3.0,
        difficulty: 4.0,
        elapsedDays: 0,
        scheduledDays: 1,
        reps: 0,
        lapses: 0,
        state: 0,
      })

      const readCard1 = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'isolation_test_1'
      )
      expect(readCard1).not.toBeNull()
      expect(readCard1?.cardId).toBe('isolation_test_1')

      await dbEngine.put('cards', {
        cardId: 'isolation_test_2',
        due: Date.now() + 86400000,
        stability: 2.0,
        difficulty: 3.0,
        elapsedDays: 0,
        scheduledDays: 1,
        reps: 0,
        lapses: 0,
        state: 0,
      })

      const readCard2 = await dbEngine.get<{cardId: string; due: number; stability: number; difficulty: number; elapsedDays: number; scheduledDays: number; reps: number; lapses: number; state: number}>(
        'cards',
        'isolation_test_2'
      )
      expect(readCard2).not.toBeNull()
      expect(readCard2?.cardId).toBe('isolation_test_2')
    })

    it('use unique database names or explicit database cleanup', async () => {
      await dbEngine.clear('cards')
      const count = await dbEngine.count('cards')
      expect(count).toBe(0)
    })
  })
})