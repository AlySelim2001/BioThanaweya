/**
 * @module src/services/haptics.ts
 * @description Native Web Vibration API wrapper for tactile haptic feedback in BioThanaweya.
 * Provides microsecond vibration patterns for mobile touch ergonomics with graceful degradation.
 */

const HAPTICS_STORAGE_KEY = 'biothanaweya_haptics_enabled_v1';

class HapticFeedbackService {
  private enabled: boolean;

  constructor() {
    this.enabled = this.readStoredPreference();
  }

  private readStoredPreference(): boolean {
    if (typeof window === 'undefined') return true;
    try {
      const stored = localStorage.getItem(HAPTICS_STORAGE_KEY);
      if (stored !== null) {
        return stored === 'true';
      }
    } catch {
      // Ignore storage access errors
    }
    return true; // Enabled by default
  }

  /**
   * Checks whether the native Web Vibration API is supported in this browser.
   */
  public isSupported(): boolean {
    return typeof navigator !== 'undefined' && typeof navigator.vibrate === 'function';
  }

  /**
   * Checks whether tactile haptic feedback is currently enabled by user preference.
   */
  public isEnabled(): boolean {
    return this.enabled && this.isSupported();
  }

  /**
   * Updates the haptic feedback user preference and persists to local storage.
   */
  public setEnabled(enable: boolean): void {
    this.enabled = enable;
    if (typeof window !== 'undefined') {
      try {
        localStorage.setItem(HAPTICS_STORAGE_KEY, String(enable));
      } catch {
        // Ignore storage errors
      }
    }
    if (enable && this.isSupported()) {
      this.vibrate(12); // Sensory confirmation tap
    }
  }

  /**
   * Internal safe vibration trigger.
   * Priority: Native Android JavaScript Bridge -> Web Vibration API
   */
  private vibrate(pattern: number | number[], intensity: 'light' | 'medium' | 'heavy' | 'success' | 'error' = 'light'): boolean {
    if (!this.enabled) {
      return false;
    }

    // Check for Native Android Bridge first
    if (typeof window !== 'undefined' && (window as unknown as { BioThanaweyaNative?: { triggerHaptic: (i: string) => void } }).BioThanaweyaNative) {
      try {
        (window as unknown as { BioThanaweyaNative: { triggerHaptic: (i: string) => void } }).BioThanaweyaNative.triggerHaptic(intensity);
        return true;
      } catch {
        // Fall back to Web Vibration API
      }
    }

    if (!this.isSupported()) {
      return false;
    }
    try {
      return navigator.vibrate(pattern);
    } catch {
      return false;
    }
  }

  /**
   * Ultra-subtle single tick (10ms) accompanying screen navigation.
   */
  public tick(): boolean {
    return this.vibrate(10, 'light');
  }

  /**
   * Tactile pop (16ms) on modal dialog opening and closing.
   */
  public pop(): boolean {
    return this.vibrate(16, 'medium');
  }

  /**
   * Rhythmic celebration burst [10, 40, 15] on XP milestones and badge achievements.
   */
  public celebrate(): boolean {
    return this.vibrate([10, 40, 15], 'success');
  }

  /**
   * Warning vibration pattern [25, 50, 25] on formula input errors or ministry deductions.
   */
  public warning(): boolean {
    return this.vibrate([25, 50, 25], 'error');
  }

  /**
   * Halts any active vibration pulse immediately.
   */
  public stop(): boolean {
    if (!this.isSupported()) return false;
    try {
      return navigator.vibrate(0);
    } catch {
      return false;
    }
  }
}

export const haptics = new HapticFeedbackService();
