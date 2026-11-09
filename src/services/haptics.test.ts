/**
 * @module src/services/haptics.test.ts
 * @description Vitest unit tests for the Haptic Feedback API service.
 */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { haptics } from './haptics';

// Defensive polyfill for headless Node test environment
if (typeof globalThis.navigator === 'undefined') {
  (globalThis as unknown as { navigator: { vibrate: (p: unknown) => boolean } }).navigator = {
    vibrate: () => true,
  };
} else if (typeof globalThis.navigator.vibrate !== 'function') {
  (globalThis.navigator as unknown as { vibrate: (p: unknown) => boolean }).vibrate = () => true;
}

describe('HapticFeedbackService', () => {
  let vibrateMock: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    // Ensure clean window environment without residual native bridge
    if (typeof window !== 'undefined') {
      delete (window as unknown as { BioThanaweyaNative?: unknown }).BioThanaweyaNative;
    }
    vibrateMock = vi.fn().mockReturnValue(true);
    Object.defineProperty(globalThis.navigator, 'vibrate', {
      value: vibrateMock,
      writable: true,
      configurable: true,
    });
    haptics.setEnabled(true);
    vibrateMock.mockClear();
  });

  afterEach(() => {
    if (typeof window !== 'undefined') {
      delete (window as unknown as { BioThanaweyaNative?: unknown }).BioThanaweyaNative;
    }
    vi.restoreAllMocks();
  });

  it('detects vibration API support', () => {
    expect(haptics.isSupported()).toBe(true);
  });

  it('triggers 10ms pulse on tick()', () => {
    const result = haptics.tick();
    expect(result).toBe(true);
    expect(vibrateMock).toHaveBeenCalledWith(10);
  });

  it('triggers 16ms pulse on pop()', () => {
    const result = haptics.pop();
    expect(result).toBe(true);
    expect(vibrateMock).toHaveBeenCalledWith(16);
  });

  it('triggers rhythmic celebration pattern on celebrate()', () => {
    const result = haptics.celebrate();
    expect(result).toBe(true);
    expect(vibrateMock).toHaveBeenCalledWith([10, 40, 15]);
  });

  it('respects user disabling haptic feedback', () => {
    haptics.setEnabled(false);
    vibrateMock.mockClear();

    const result = haptics.tick();
    expect(result).toBe(false);
    expect(vibrateMock).not.toHaveBeenCalled();
  });

  it('stops ongoing vibration with pattern 0', () => {
    const result = haptics.stop();
    expect(result).toBe(true);
    expect(vibrateMock).toHaveBeenCalledWith(0);
  });
});
