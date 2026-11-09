/**
 * @module src/components/StorageVaultModal.tsx
 * @description Interactive visual management modal for the client-side IndexedDB persistent storage engine.
 * Provides quota metrics, JSON export/import, rolling snapshot inspection, and disaster recovery.
 */

import React, { useState, useEffect, useRef } from 'react';
import {
  X,
  Database,
  Download,
  Upload,
  RotateCcw,
  HardDrive,
  ShieldCheck,
  Clock,
  Layers,
  FileCheck2,
  AlertTriangle,
  RefreshCw,
  Sparkles,
} from 'lucide-react';
import { dbEngine, StorageQuotaMetrics, BackupSnapshot } from '../services/db/indexedDbEngine';
import { BackupService, ImportResult } from '../services/db/backupService';
import { StorageService, SyncState } from '../services/storage';

interface StorageVaultModalProps {
  isOpen: boolean;
  onClose: () => void;
  onDataRestored?: () => void;
}

export const StorageVaultModal: React.FC<StorageVaultModalProps> = ({
  isOpen,
  onClose,
  onDataRestored,
}) => {
  const [metrics, setMetrics] = useState<StorageQuotaMetrics | null>(null);
  const [snapshots, setSnapshots] = useState<BackupSnapshot[]>([]);
  const [syncState, setSyncState] = useState<SyncState>('synced');
  const [isExporting, setIsExporting] = useState(false);
  const [isImporting, setIsImporting] = useState(false);
  const [importNotice, setImportNotice] = useState<ImportResult | null>(null);
  const [activeTab, setActiveTab] = useState<'overview' | 'snapshots'>('overview');
  const [resetConfirming, setResetConfirming] = useState(false);

  const fileInputRef = useRef<HTMLInputElement>(null);

  // Load metrics & snapshots when opened
  const refreshStorageData = async () => {
    try {
      const [m, s] = await Promise.all([
        dbEngine.getStorageMetrics(),
        BackupService.getSnapshots(),
      ]);
      setMetrics(m);
      setSnapshots(s);
    } catch (e) {
      console.error('Failed to load storage metrics', e);
    }
  };

  useEffect(() => {
    if (!isOpen) return;
    refreshStorageData();
    const unsubscribe = StorageService.onSyncStatusChange((state) => {
      setSyncState(state);
      if (state === 'synced') {
        refreshStorageData();
      }
    });
    return () => unsubscribe();
  }, [isOpen]);

  if (!isOpen) return null;

  const handleExport = async () => {
    setIsExporting(true);
    try {
      await BackupService.exportDatabaseToJson();
    } catch (err) {
      console.error('Export error:', err);
    } finally {
      setIsExporting(false);
    }
  };

  const handleFileSelected = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setIsImporting(true);
    setImportNotice(null);

    const result = await BackupService.importDatabaseFromJson(file);
    setIsImporting(false);
    setImportNotice(result);

    if (result.success) {
      await StorageService.reloadFromIndexedDb();
      await refreshStorageData();
      if (onDataRestored) {
        onDataRestored();
      }
    }

    // Reset input
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleRestoreSnapshot = async (snapshotId: string) => {
    if (!confirm('Are you sure you want to restore this snapshot? Current unsaved changes will be superseded.')) {
      return;
    }
    const success = await BackupService.restoreSnapshot(snapshotId);
    if (success) {
      await StorageService.reloadFromIndexedDb();
      await refreshStorageData();
      setImportNotice({
        success: true,
        message: 'Snapshot restored successfully.',
      });
      if (onDataRestored) {
        onDataRestored();
      }
    } else {
      setImportNotice({
        success: false,
        message: 'Failed to restore selected snapshot.',
      });
    }
  };

  const handleFactoryReset = async () => {
    await StorageService.resetAll();
    await refreshStorageData();
    setResetConfirming(false);
    setImportNotice({
      success: true,
      message: 'Database cleanly reset and re-seeded with official curriculum standards.',
    });
    if (onDataRestored) {
      onDataRestored();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-200">
      <div className="bg-[#0D1527] border border-teal-500/30 rounded-2xl max-w-2xl w-full max-h-[90vh] flex flex-col shadow-2xl shadow-teal-950/50 overflow-hidden">
        {/* Header */}
        <div className="p-5 border-b border-slate-800 flex items-center justify-between bg-gradient-to-r from-[#0D1527] to-[#12203F]">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-teal-500/10 border border-teal-500/30 flex items-center justify-center text-teal-400">
              <Database className="w-5 h-5" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-lg font-bold text-white tracking-wide">
                  Storage Vault & Database Engine
                </h2>
                <span
                  className={`flex items-center gap-1 text-[11px] font-bold px-2 py-0.5 rounded-full border ${
                    syncState === 'synced'
                      ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                      : syncState === 'saving'
                      ? 'bg-amber-500/10 text-amber-400 border-amber-500/30'
                      : 'bg-rose-500/10 text-rose-400 border-rose-500/30'
                  }`}
                >
                  <span
                    className={`w-1.5 h-1.5 rounded-full ${
                      syncState === 'synced'
                        ? 'bg-emerald-400'
                        : syncState === 'saving'
                        ? 'bg-amber-400 animate-pulse'
                        : 'bg-rose-400'
                    }`}
                  />
                  {syncState === 'synced' ? 'IndexedDB Synced' : syncState === 'saving' ? 'Persisting...' : 'Sync Error'}
                </span>
              </div>
              <p className="text-xs text-slate-400">
                100% Client-Side Native IndexedDB • Zero Dependencies • FOSS
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 text-slate-400 hover:text-white rounded-lg hover:bg-slate-800 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Tab Navigation */}
        <div className="px-5 pt-3 border-b border-slate-800 flex gap-4 text-xs font-bold">
          <button
            onClick={() => setActiveTab('overview')}
            className={`pb-2.5 transition border-b-2 flex items-center gap-1.5 ${
              activeTab === 'overview'
                ? 'border-teal-400 text-teal-300'
                : 'border-transparent text-slate-400 hover:text-slate-200'
            }`}
          >
            <HardDrive className="w-3.5 h-3.5" />
            Storage Overview & Portability
          </button>
          <button
            onClick={() => setActiveTab('snapshots')}
            className={`pb-2.5 transition border-b-2 flex items-center gap-1.5 ${
              activeTab === 'snapshots'
                ? 'border-teal-400 text-teal-300'
                : 'border-transparent text-slate-400 hover:text-slate-200'
            }`}
          >
            <Clock className="w-3.5 h-3.5" />
            Rolling Snapshots ({snapshots.length})
          </button>
        </div>

        {/* Content Body */}
        <div className="p-5 overflow-y-auto space-y-5 flex-1">
          {/* Status / Alert Notices */}
          {importNotice && (
            <div
              className={`p-3.5 rounded-xl border flex items-start gap-3 text-xs animate-in fade-in ${
                importNotice.success
                  ? 'bg-emerald-950/40 border-emerald-500/40 text-emerald-200'
                  : 'bg-rose-950/40 border-rose-500/40 text-rose-200'
              }`}
            >
              {importNotice.success ? (
                <FileCheck2 className="w-4 h-4 text-emerald-400 flex-shrink-0 mt-0.5" />
              ) : (
                <AlertTriangle className="w-4 h-4 text-rose-400 flex-shrink-0 mt-0.5" />
              )}
              <div className="space-y-0.5">
                <div className="font-bold">{importNotice.message}</div>
                {importNotice.counts && (
                  <div className="text-[11px] text-emerald-300/80">
                    Restored: {importNotice.counts.cards} flashcards, {importNotice.counts.masteries} topics,{' '}
                    {importNotice.counts.exams} exam reports.
                  </div>
                )}
                {importNotice.errorDetails && (
                  <div className="text-[11px] font-mono text-rose-300/80">{importNotice.errorDetails}</div>
                )}
              </div>
            </div>
          )}

          {activeTab === 'overview' ? (
            <>
              {/* Storage Quota Card */}
              <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 space-y-3">
                <div className="flex items-center justify-between text-xs">
                  <span className="font-semibold text-slate-300 flex items-center gap-1.5">
                    <ShieldCheck className="w-4 h-4 text-teal-400" />
                    IndexedDB Storage Consumption
                  </span>
                  <span className="font-mono text-teal-300 font-bold">
                    {metrics ? `${metrics.usedFormatted} used` : 'Calculating...'}
                  </span>
                </div>

                {/* Progress bar */}
                <div className="w-full bg-slate-800 h-2 rounded-full overflow-hidden">
                  <div
                    className="bg-gradient-to-r from-teal-500 to-cyan-400 h-full rounded-full transition-all duration-500"
                    style={{ width: `${Math.max(2, metrics?.usagePercent || 1)}%` }}
                  />
                </div>

                <div className="flex justify-between items-center text-[11px] text-slate-500">
                  <span>Quota: ~{metrics?.quotaFormatted || '1 GB'} (Persistent)</span>
                  <span>Unlimited capacity vs legacy 5MB localStorage</span>
                </div>

                {/* Entities Matrix */}
                <div className="grid grid-cols-4 gap-2 pt-2 border-t border-slate-800/80 text-center">
                  <div className="p-2 rounded-lg bg-slate-950/60 border border-slate-800">
                    <div className="text-base font-extrabold text-white">
                      {metrics?.counts.cards ?? '—'}
                    </div>
                    <div className="text-[10px] text-slate-400 uppercase tracking-wider">FSRS Cards</div>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-950/60 border border-slate-800">
                    <div className="text-base font-extrabold text-teal-400">
                      {metrics?.counts.masteries ?? '—'}
                    </div>
                    <div className="text-[10px] text-slate-400 uppercase tracking-wider">Topics</div>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-950/60 border border-slate-800">
                    <div className="text-base font-extrabold text-cyan-400">
                      {metrics?.counts.exams ?? '—'}
                    </div>
                    <div className="text-[10px] text-slate-400 uppercase tracking-wider">Exams</div>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-950/60 border border-slate-800">
                    <div className="text-base font-extrabold text-indigo-400">
                      {metrics?.counts.backups ?? '—'}
                    </div>
                    <div className="text-[10px] text-slate-400 uppercase tracking-wider">Snapshots</div>
                  </div>
                </div>
              </div>

              {/* Data Portability (Export & Import) */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {/* Export Action */}
                <div className="p-4 rounded-xl bg-[#0F1C36] border border-cyan-500/20 flex flex-col justify-between space-y-3">
                  <div>
                    <div className="flex items-center gap-2 text-cyan-300 font-bold text-xs">
                      <Download className="w-4 h-4" />
                      <span>Export JSON Vault</span>
                    </div>
                    <p className="text-[11px] text-slate-400 mt-1 leading-relaxed">
                      Save a portable, typed snapshot of all FSRS intervals, topic mastery, and exam reviews directly to your device.
                    </p>
                  </div>
                  <button
                    onClick={handleExport}
                    disabled={isExporting}
                    className="w-full flex items-center justify-center gap-2 px-3 py-2 rounded-lg bg-cyan-600 hover:bg-cyan-500 text-slate-950 font-bold text-xs transition disabled:opacity-50"
                  >
                    {isExporting ? <RefreshCw className="w-3.5 h-3.5 animate-spin" /> : <Download className="w-3.5 h-3.5" />}
                    <span>{isExporting ? 'Generating...' : 'Download JSON Backup'}</span>
                  </button>
                </div>

                {/* Import Action */}
                <div className="p-4 rounded-xl bg-[#0F1C36] border border-teal-500/20 flex flex-col justify-between space-y-3">
                  <div>
                    <div className="flex items-center gap-2 text-teal-300 font-bold text-xs">
                      <Upload className="w-4 h-4" />
                      <span>Restore JSON Vault</span>
                    </div>
                    <p className="text-[11px] text-slate-400 mt-1 leading-relaxed">
                      Import a previously exported BioThanaweya backup. Automatically creates a safety snapshot before overwriting.
                    </p>
                  </div>
                  <div>
                    <input
                      type="file"
                      ref={fileInputRef}
                      onChange={handleFileSelected}
                      accept=".json,application/json"
                      className="hidden"
                    />
                    <button
                      onClick={() => fileInputRef.current?.click()}
                      disabled={isImporting}
                      className="w-full flex items-center justify-center gap-2 px-3 py-2 rounded-lg bg-teal-600 hover:bg-teal-500 text-slate-950 font-bold text-xs transition disabled:opacity-50"
                    >
                      {isImporting ? <RefreshCw className="w-3.5 h-3.5 animate-spin" /> : <Upload className="w-3.5 h-3.5" />}
                      <span>{isImporting ? 'Validating...' : 'Select JSON File'}</span>
                    </button>
                  </div>
                </div>
              </div>

              {/* Disaster Recovery / Factory Reset */}
              <div className="p-4 rounded-xl bg-rose-950/20 border border-rose-500/30 flex items-center justify-between">
                <div className="space-y-0.5">
                  <div className="text-xs font-bold text-rose-300 flex items-center gap-1.5">
                    <AlertTriangle className="w-3.5 h-3.5" />
                    Factory Re-seed & Purge
                  </div>
                  <div className="text-[11px] text-slate-400">
                    Purges all local IndexedDB records and re-seeds default curriculum packs.
                  </div>
                </div>
                {resetConfirming ? (
                  <div className="flex items-center gap-2">
                    <button
                      onClick={handleFactoryReset}
                      className="px-3 py-1.5 rounded-lg bg-rose-600 hover:bg-rose-500 text-white font-bold text-xs transition"
                    >
                      Confirm Reset
                    </button>
                    <button
                      onClick={() => setResetConfirming(false)}
                      className="px-2 py-1.5 rounded-lg bg-slate-800 text-slate-400 text-xs hover:text-white"
                    >
                      Cancel
                    </button>
                  </div>
                ) : (
                  <button
                    onClick={() => setResetConfirming(true)}
                    className="flex items-center gap-1 px-3 py-1.5 rounded-lg bg-rose-600/20 hover:bg-rose-600/30 text-rose-300 border border-rose-500/40 text-xs font-semibold transition"
                  >
                    <RotateCcw className="w-3.5 h-3.5" />
                    Reset
                  </button>
                )}
              </div>
            </>
          ) : (
            /* Snapshots Tab */
            <div className="space-y-3">
              <div className="flex items-center justify-between text-xs text-slate-400 pb-1">
                <span>Automatic rolling snapshots created during key study milestones:</span>
                <span className="font-mono text-[11px] text-teal-400">{snapshots.length} / 5 stored</span>
              </div>

              {snapshots.length === 0 ? (
                <div className="p-8 text-center rounded-xl bg-slate-900/50 border border-slate-800 text-xs text-slate-500">
                  No automated snapshots recorded yet. Complete an exam or review cards to trigger auto-snapshots.
                </div>
              ) : (
                snapshots.map((snap) => (
                  <div
                    key={snap.snapshotId}
                    className="p-3.5 rounded-xl bg-slate-900/70 border border-slate-800 flex items-center justify-between hover:border-slate-700 transition"
                  >
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-bold text-slate-200">{snap.reason}</span>
                        <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-slate-800 text-slate-400">
                          {snap.snapshotId.slice(-6)}
                        </span>
                      </div>
                      <div className="text-[11px] text-slate-500 flex items-center gap-3">
                        <span>{new Date(snap.timestamp).toLocaleString()}</span>
                        <span>•</span>
                        <span>{snap.data.cards.length} cards</span>
                        <span>•</span>
                        <span>{snap.data.exams.length} exam reviews</span>
                      </div>
                    </div>
                    <button
                      onClick={() => handleRestoreSnapshot(snap.snapshotId)}
                      className="flex items-center gap-1 px-2.5 py-1.5 rounded-lg bg-teal-500/15 hover:bg-teal-500/25 border border-teal-500/30 text-teal-300 text-xs font-semibold transition"
                    >
                      <RotateCcw className="w-3 h-3" />
                      Restore
                    </button>
                  </div>
                ))
              )}
            </div>
          )}
        </div>

        {/* Modal Footer */}
        <div className="p-4 border-t border-slate-800 bg-[#0B132B] flex items-center justify-between text-xs text-slate-400">
          <div className="flex items-center gap-1.5">
            <Sparkles className="w-3.5 h-3.5 text-teal-400" />
            <span>Encrypted in your browser sandbox • Offline-ready</span>
          </div>
          <button
            onClick={onClose}
            className="px-4 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-white font-medium transition"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};
