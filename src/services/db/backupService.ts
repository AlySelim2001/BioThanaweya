/**
 * @module src/services/db/backupService.ts
 * @description JSON Backup & Restore Engine with Schema Integrity Validation.
 * 100% FOSS, zero-dependency data migration and rolling auto-snapshots for BioThanaweya.
 */

import { FSRSCard, TopicMastery, WellbeingProfile, ExamResultReport } from '../../types';
import { dbEngine, BackupSnapshot } from './indexedDbEngine';

export interface DatabaseBackupPayload {
  schemaVersion: number;
  appName: 'BioThanaweya';
  exportedAt: string;
  metadata: {
    clientTimezone: string;
    userAgent: string;
  };
  data: {
    profile: WellbeingProfile;
    cards: FSRSCard[];
    masteries: TopicMastery[];
    exams: ExamResultReport[];
  };
}

export interface ImportResult {
  success: boolean;
  message: string;
  errorDetails?: string;
  counts?: {
    cards: number;
    masteries: number;
    exams: number;
  };
}

const MAX_ROLLING_SNAPSHOTS = 5;

export const BackupService = {
  /**
   * Generates a complete JSON backup of all IndexedDB stores and triggers browser download.
   */
  async exportDatabaseToJson(): Promise<void> {
    const [profileRecord, cards, masteries, exams] = await Promise.all([
      dbEngine.get<{ id: string; profile: WellbeingProfile }>('profile', 'current'),
      dbEngine.getAll<FSRSCard>('cards'),
      dbEngine.getAll<TopicMastery>('masteries'),
      dbEngine.getAll<ExamResultReport>('exams'),
    ]);

    const backupPayload: DatabaseBackupPayload = {
      schemaVersion: 1,
      appName: 'BioThanaweya',
      exportedAt: new Date().toISOString(),
      metadata: {
        clientTimezone: Intl.DateTimeFormat().resolvedOptions().timeZone || 'UTC',
        userAgent: typeof navigator !== 'undefined' ? navigator.userAgent : 'Unknown',
      },
      data: {
        profile: profileRecord?.profile || {
          streakDays: 1,
          totalXp: 0,
          earnedBadges: [],
          examAnxietyScore: 20,
          studyChunkMinutes: 25,
          motivationalQuote: '',
        },
        cards,
        masteries,
        exams,
      },
    };

    const jsonString = JSON.stringify(backupPayload, null, 2);
    const blob = new Blob([jsonString], { type: 'application/json;charset=utf-8' });
    const url = URL.createObjectURL(blob);

    const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, 19);
    const filename = `biothanaweya_vault_backup_${timestamp}.json`;

    const downloadAnchor = document.createElement('a');
    downloadAnchor.href = url;
    downloadAnchor.download = filename;
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    document.body.removeChild(downloadAnchor);
    URL.revokeObjectURL(url);
  },

  /**
   * Validates and imports a JSON backup into the IndexedDB database.
   */
  async importDatabaseFromJson(file: File): Promise<ImportResult> {
    try {
      const fileContent = await file.text();
      let payload: unknown;

      try {
        payload = JSON.parse(fileContent);
      } catch {
        return {
          success: false,
          message: 'Invalid JSON format. Please verify the backup file integrity.',
        };
      }

      if (!payload || typeof payload !== 'object') {
        return {
          success: false,
          message: 'Malformed payload: Expected root JSON object.',
        };
      }

      const candidate = payload as Partial<DatabaseBackupPayload>;

      // Header validation
      if (candidate.appName !== 'BioThanaweya') {
        return {
          success: false,
          message: 'Incompatible backup file. Expected BioThanaweya archive.',
        };
      }

      if (!candidate.data || typeof candidate.data !== 'object') {
        return {
          success: false,
          message: 'Malformed backup: Missing "data" container.',
        };
      }

      const { data } = candidate;

      // Validate cards
      if (!Array.isArray(data.cards)) {
        return {
          success: false,
          message: 'Malformed backup: "cards" must be an array.',
        };
      }

      // Validate masteries
      if (!Array.isArray(data.masteries)) {
        return {
          success: false,
          message: 'Malformed backup: "masteries" must be an array.',
        };
      }

      // Validate exams
      if (!Array.isArray(data.exams)) {
        return {
          success: false,
          message: 'Malformed backup: "exams" must be an array.',
        };
      }

      // Create safety auto-snapshot of current state prior to overwriting
      await this.createAutoSnapshot('Pre-import safety snapshot');

      // Clear existing stores
      await Promise.all([
        dbEngine.clear('cards'),
        dbEngine.clear('masteries'),
        dbEngine.clear('exams'),
        dbEngine.clear('profile'),
      ]);

      // Write imported data
      await Promise.all([
        dbEngine.putMany('cards', data.cards),
        dbEngine.putMany('masteries', data.masteries),
        dbEngine.putMany('exams', data.exams),
        data.profile
          ? dbEngine.put('profile', { id: 'current', profile: data.profile })
          : Promise.resolve(),
      ]);

      return {
        success: true,
        message: 'Database successfully restored from JSON backup.',
        counts: {
          cards: data.cards.length,
          masteries: data.masteries.length,
          exams: data.exams.length,
        },
      };
    } catch (err: unknown) {
      const errorMsg = err instanceof Error ? err.message : String(err);
      return {
        success: false,
        message: 'Unexpected failure during database restore.',
        errorDetails: errorMsg,
      };
    }
  },

  /**
   * Creates an automated rolling snapshot stored inside the local IndexedDB 'backups' store.
   */
  async createAutoSnapshot(reason: string): Promise<string> {
    try {
      const [profileRecord, cards, masteries, exams] = await Promise.all([
        dbEngine.get<{ id: string; profile: WellbeingProfile }>('profile', 'current'),
        dbEngine.getAll<FSRSCard>('cards'),
        dbEngine.getAll<TopicMastery>('masteries'),
        dbEngine.getAll<ExamResultReport>('exams'),
      ]);

      const snapshotId = `snap_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`;
      const snapshot: BackupSnapshot = {
        snapshotId,
        timestamp: new Date().toISOString(),
        reason,
        data: {
          profile: profileRecord?.profile || {
            streakDays: 1,
            totalXp: 0,
            earnedBadges: [],
            examAnxietyScore: 20,
            studyChunkMinutes: 25,
            motivationalQuote: '',
          },
          cards,
          masteries,
          exams,
        },
      };

      await dbEngine.put('backups', snapshot);

      // Clean up excess snapshots maintaining rolling limit
      const allSnapshots = await dbEngine.getAll<BackupSnapshot>('backups');
      if (allSnapshots.length > MAX_ROLLING_SNAPSHOTS) {
        allSnapshots.sort(
          (a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime()
        );
        const toDelete = allSnapshots.slice(0, allSnapshots.length - MAX_ROLLING_SNAPSHOTS);
        for (const item of toDelete) {
          await dbEngine.delete('backups', item.snapshotId);
        }
      }

      return snapshotId;
    } catch {
      return '';
    }
  },

  /**
   * Retrieves all available rolling snapshots.
   */
  async getSnapshots(): Promise<BackupSnapshot[]> {
    try {
      const snapshots = await dbEngine.getAll<BackupSnapshot>('backups');
      return snapshots.sort(
        (a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
      );
    } catch {
      return [];
    }
  },

  /**
   * Restores database state from a specific local snapshot.
   */
  async restoreSnapshot(snapshotId: string): Promise<boolean> {
    try {
      const snapshot = await dbEngine.get<BackupSnapshot>('backups', snapshotId);
      if (!snapshot) return false;

      // Clear current stores
      await Promise.all([
        dbEngine.clear('cards'),
        dbEngine.clear('masteries'),
        dbEngine.clear('exams'),
        dbEngine.clear('profile'),
      ]);

      // Restore data
      await Promise.all([
        dbEngine.putMany('cards', snapshot.data.cards),
        dbEngine.putMany('masteries', snapshot.data.masteries),
        dbEngine.putMany('exams', snapshot.data.exams),
        dbEngine.put('profile', { id: 'current', profile: snapshot.data.profile }),
      ]);

      return true;
    } catch {
      return false;
    }
  },
};
