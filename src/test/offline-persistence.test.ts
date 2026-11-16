import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { FSRSCard, WellbeingProfile } from '../types';
import type { BackupSnapshot } from '../services/db/indexedDbEngine';

interface DbEngineMock {
  get: ReturnType<typeof vi.fn>;
  getAll: ReturnType<typeof vi.fn>;
  put: ReturnType<typeof vi.fn>;
  putMany: ReturnType<typeof vi.fn>;
  clear: ReturnType<typeof vi.fn>;
}

interface StorageHarness {
  storage: typeof import('../services/storage').StorageService;
  db: DbEngineMock;
  snapshot: ReturnType<typeof vi.fn>;
  localValues: Map<string, string>;
}

const createStorageHarness = async (): Promise<StorageHarness> => {
  vi.resetModules();
  const localValues = new Map<string, string>();
  const localStorageMock: Storage = {
    get length(): number { return localValues.size; },
    clear: vi.fn(() => localValues.clear()),
    getItem: vi.fn((key: string) => localValues.get(key) ?? null),
    key: vi.fn((index: number) => [...localValues.keys()][index] ?? null),
    removeItem: vi.fn((key: string) => localValues.delete(key)),
    setItem: vi.fn((key: string, value: string) => localValues.set(key, value)),
  };
  const db: DbEngineMock = {
    get: vi.fn(async () => null),
    getAll: vi.fn(async () => []),
    put: vi.fn(async () => undefined),
    putMany: vi.fn(async () => undefined),
    clear: vi.fn(async () => undefined),
  };
  const snapshot = vi.fn(async (_reason: string): Promise<BackupSnapshot | null> => null);

  vi.stubGlobal('window', {});
  vi.stubGlobal('localStorage', localStorageMock);
  vi.doMock('../services/db/indexedDbEngine', () => ({ dbEngine: db }));
  vi.doMock('../services/db/backupService', () => ({
    BackupService: { createAutoSnapshot: snapshot },
  }));

  const { StorageService } = await import('../services/storage');
  return { storage: StorageService, db, snapshot, localValues };
};

describe('offline-first web storage persistence', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it('round-trips saved student progress through JSON and the database boundary', async () => {
    const { storage, db, localValues } = await createStorageHarness();
    const profile: WellbeingProfile = {
      streakDays: 6,
      totalXp: 1240,
      earnedBadges: [],
      examAnxietyScore: 18,
      studyChunkMinutes: 25,
      motivationalQuote: 'Keep learning.',
    };

    storage.saveProfile(profile);

    const serialized = localValues.get('biothanaweya_wellbeing_profile_v1');
    expect(serialized).toBeDefined();
    expect(JSON.parse(serialized as string) as WellbeingProfile).toEqual(profile);
    expect(db.put).toHaveBeenCalledWith('profile', { id: 'current', profile });
    expect(storage.getProfile()).toEqual(profile);
  });

  it('initializes safe defaults if legacy local keys are missing', async () => {
    const { storage, db } = await createStorageHarness();

    await storage.init();

    expect(storage.getProfile().totalXp).toBeGreaterThanOrEqual(0);
    expect(storage.getFsrsCards().length).toBeGreaterThan(0);
    expect(db.put).toHaveBeenCalledWith('profile', {
      id: 'current',
      profile: storage.getProfile(),
    });
    expect(db.putMany).toHaveBeenCalledWith('cards', storage.getFsrsCards());
  });

  it('recovers from corrupted legacy JSON without leaking a rejected promise', async () => {
    const { storage, db, localValues } = await createStorageHarness();
    localValues.set('biothanaweya_wellbeing_profile_v1', '{not-json');
    localValues.set('biothanaweya_fsrs_cards_v1', '[broken');
    localValues.set('biothanaweya_topic_masteries_v1', 'null');
    localValues.set('biothanaweya_exam_history_v1', '{');

    await expect(storage.init()).resolves.toBeUndefined();

    expect(storage.getProfile().totalXp).toBeGreaterThanOrEqual(0);
    expect(storage.getFsrsCards().length).toBeGreaterThan(0);
    expect(db.put).toHaveBeenCalled();
  });

  it('loads persisted progress and FSRS cards when an offline database is available', async () => {
    const profile: WellbeingProfile = {
      streakDays: 2,
      totalXp: 300,
      earnedBadges: [],
      examAnxietyScore: 25,
      studyChunkMinutes: 30,
      motivationalQuote: 'Progress saved locally.',
    };
    const persistedCard: FSRSCard = {
      cardId: 'offline-card',
      due: 1736942400000,
      stability: 3.173,
      difficulty: 5,
      elapsedDays: 0,
      scheduledDays: 1,
      reps: 1,
      lapses: 0,
      state: 2,
      lastReview: 1736856000000,
    };
    const harness = await createStorageHarness();
    harness.db.get.mockResolvedValue({ id: 'current', profile });
    harness.db.getAll.mockImplementation(async (storeName: string) => {
      if (storeName === 'cards') return [persistedCard];
      return [];
    });

    await harness.storage.init();

    expect(harness.storage.getProfile()).toEqual(profile);
    expect(harness.storage.getFsrsCards()).toContainEqual(persistedCard);
    expect(harness.db.getAll).toHaveBeenCalledWith('cards');
    expect(harness.snapshot).not.toHaveBeenCalled();
  });

  it('removes its sync listener when the returned unsubscribe function is called', async () => {
    const { storage } = await createStorageHarness();
    const listener = vi.fn();
    const unsubscribe = storage.onSyncStatusChange(listener);

    unsubscribe();
    storage.saveProfile({
      streakDays: 1,
      totalXp: 1,
      earnedBadges: [],
      examAnxietyScore: 0,
      studyChunkMinutes: 25,
      motivationalQuote: 'Test fixture.',
    });

    expect(listener).toHaveBeenCalledTimes(1);
  });
});
