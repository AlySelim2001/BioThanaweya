/**
 * @module src/services/db/indexedDbEngine.ts
 * @description Native, 100% FOSS Typed IndexedDB Database Engine for BioThanaweya.
 * Provides zero-overhead, asynchronous transactional persistence with automatic migration,
 * completely replacing synchronous 5MB localStorage limits.
 */

import { FSRSCard, TopicMastery, WellbeingProfile, ExamResultReport } from '../../types';

export const DB_NAME = 'BioThanaweyaDB_v1';
export const DB_VERSION = 1;

export interface StorageQuotaMetrics {
  usedBytes: number;
  quotaBytes: number;
  usedFormatted: string;
  quotaFormatted: string;
  usagePercent: number;
  counts: {
    cards: number;
    masteries: number;
    exams: number;
    backups: number;
  };
}

export interface BackupSnapshot {
  snapshotId: string;
  timestamp: string;
  reason: string;
  data: {
    profile: WellbeingProfile;
    cards: FSRSCard[];
    masteries: TopicMastery[];
    exams: ExamResultReport[];
  };
}

class IndexedDbEngine {
  private dbPromise: Promise<IDBDatabase> | null = null;

  public async getDb(): Promise<IDBDatabase> {
    if (this.dbPromise) {
      return this.dbPromise;
    }

    this.dbPromise = new Promise<IDBDatabase>((resolve, reject) => {
      if (typeof window === 'undefined' || !window.indexedDB) {
        reject(new Error('IndexedDB is not supported in this runtime environment.'));
        return;
      }

      const request = window.indexedDB.open(DB_NAME, DB_VERSION);

      request.onupgradeneeded = (event: IDBVersionChangeEvent) => {
        const db = (event.target as IDBOpenDBRequest).result;

        // Store 1: FSRS Cards
        if (!db.objectStoreNames.contains('cards')) {
          const cardStore = db.createObjectStore('cards', { keyPath: 'cardId' });
          cardStore.createIndex('due', 'due', { unique: false });
          cardStore.createIndex('state', 'state', { unique: false });
        }

        // Store 2: Topic Masteries
        if (!db.objectStoreNames.contains('masteries')) {
          const masteryStore = db.createObjectStore('masteries', { keyPath: 'topicId' });
          masteryStore.createIndex('masteryLevel', 'masteryLevel', { unique: false });
          masteryStore.createIndex('stability', 'stability', { unique: false });
        }

        // Store 3: Exam History
        if (!db.objectStoreNames.contains('exams')) {
          const examStore = db.createObjectStore('exams', { keyPath: 'id', autoIncrement: true });
          examStore.createIndex('percentage', 'percentage', { unique: false });
        }

        // Store 4: Wellbeing Profile
        if (!db.objectStoreNames.contains('profile')) {
          db.createObjectStore('profile', { keyPath: 'id' });
        }

        // Store 5: Automated Rolling Backups
        if (!db.objectStoreNames.contains('backups')) {
          const backupStore = db.createObjectStore('backups', { keyPath: 'snapshotId' });
          backupStore.createIndex('timestamp', 'timestamp', { unique: false });
        }

        // Store 6: Sync Metadata
        if (!db.objectStoreNames.contains('sync_meta')) {
          db.createObjectStore('sync_meta', { keyPath: 'key' });
        }
      };

      request.onsuccess = (event: Event) => {
        const db = (event.target as IDBOpenDBRequest).result;
        resolve(db);
      };

      request.onerror = (event: Event) => {
        const error = (event.target as IDBOpenDBRequest).error;
        reject(error || new Error('Failed to open IndexedDB database.'));
      };
    });

    return this.dbPromise;
  }

  public async get<T>(storeName: string, key: IDBValidKey): Promise<T | null> {
    const db = await this.getDb();
    return new Promise<T | null>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readonly');
      const store = transaction.objectStore(storeName);
      const request = store.get(key);

      request.onsuccess = () => resolve(request.result || null);
      request.onerror = () => reject(request.error);
    });
  }

  public async getAll<T>(storeName: string): Promise<T[]> {
    const db = await this.getDb();
    return new Promise<T[]>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readonly');
      const store = transaction.objectStore(storeName);
      const request = store.getAll();

      request.onsuccess = () => resolve(request.result || []);
      request.onerror = () => reject(request.error);
    });
  }

  public async put<T>(storeName: string, value: T): Promise<void> {
    const db = await this.getDb();
    return new Promise<void>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readwrite');
      const store = transaction.objectStore(storeName);
      const request = store.put(value);

      request.onsuccess = () => resolve();
      request.onerror = () => reject(request.error);
    });
  }

  public async putMany<T>(storeName: string, items: T[]): Promise<void> {
    if (items.length === 0) return;
    const db = await this.getDb();
    return new Promise<void>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readwrite');
      const store = transaction.objectStore(storeName);

      items.forEach((item) => store.put(item));

      transaction.oncomplete = () => resolve();
      transaction.onerror = () => reject(transaction.error);
    });
  }

  public async delete(storeName: string, key: IDBValidKey): Promise<void> {
    const db = await this.getDb();
    return new Promise<void>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readwrite');
      const store = transaction.objectStore(storeName);
      const request = store.delete(key);

      request.onsuccess = () => resolve();
      request.onerror = () => reject(request.error);
    });
  }

  public async count(storeName: string): Promise<number> {
    const db = await this.getDb();
    return new Promise<number>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readonly');
      const store = transaction.objectStore(storeName);
      const request = store.count();

      request.onsuccess = () => resolve(request.result || 0);
      request.onerror = () => reject(request.error);
    });
  }

  public async clear(storeName: string): Promise<void> {
    const db = await this.getDb();
    return new Promise<void>((resolve, reject) => {
      const transaction = db.transaction([storeName], 'readwrite');
      const store = transaction.objectStore(storeName);
      const request = store.clear();

      request.onsuccess = () => resolve();
      request.onerror = () => reject(request.error);
    });
  }

  public async getStorageMetrics(): Promise<StorageQuotaMetrics> {
    let usedBytes = 0;
    let quotaBytes = 1024 * 1024 * 1024; // 1GB default fallback

    if (navigator.storage && navigator.storage.estimate) {
      try {
        const estimate = await navigator.storage.estimate();
        usedBytes = estimate.usage || 0;
        quotaBytes = estimate.quota || quotaBytes;
      } catch {
        // Fallback to manual estimation
      }
    }

    const [cardCount, masteryCount, examCount, backupCount] = await Promise.all([
      this.count('cards').catch(() => 0),
      this.count('masteries').catch(() => 0),
      this.count('exams').catch(() => 0),
      this.count('backups').catch(() => 0),
    ]);

    const formatBytes = (bytes: number): string => {
      if (bytes < 1024) return `${bytes} B`;
      if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
      return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
    };

    return {
      usedBytes,
      quotaBytes,
      usedFormatted: formatBytes(usedBytes),
      quotaFormatted: formatBytes(quotaBytes),
      usagePercent: Math.min(100, Math.round((usedBytes / quotaBytes) * 100)),
      counts: {
        cards: cardCount,
        masteries: masteryCount,
        exams: examCount,
        backups: backupCount,
      },
    };
  }
}

export const dbEngine = new IndexedDbEngine();
