/**
 * @module src/server/middleware/circuitBreaker.ts
 * @description Proactive Memory Circuit Breaker Middleware.
 * Protects Node.js processes running on Android (Termux/PRoot) from being abruptly killed
 * by the Android Low Memory Killer (LMK) during memory spikes.
 */

import { Request, Response, NextFunction } from 'express';
import { logger } from '../utils/logger';

export interface CircuitBreakerOptions {
  /** Maximum Resident Set Size in Megabytes before shedding non-health requests */
  maxRssMb: number;
  /** Warning threshold where warning logs begin triggering */
  warnRssMb: number;
  /** Retry-After duration in seconds sent to clients during load shedding */
  retryAfterSec: number;
}

const isProd = process.env.NODE_ENV === 'production';
const envLimit = process.env.MEMORY_LIMIT_MB ? parseInt(process.env.MEMORY_LIMIT_MB, 10) : null;

const DEFAULT_OPTIONS: CircuitBreakerOptions = {
  maxRssMb: envLimit || (isProd ? 128 : 512),
  warnRssMb: envLimit ? Math.floor(envLimit * 0.8) : (isProd ? 100 : 400),
  retryAfterSec: 5,
};

export function createMemoryCircuitBreaker(options: Partial<CircuitBreakerOptions> = {}) {
  const config = { ...DEFAULT_OPTIONS, ...options };

  return (req: Request, res: Response, next: NextFunction): void => {
    // Health and diagnostic probes always bypass circuit breaker to ensure monitoring survives
    if (req.path === '/api/health') {
      return next();
    }

    const mem = process.memoryUsage();
    const currentRssMb = mem.rss / 1024 / 1024;

    // Hard Limit Trip: Shed load immediately to prevent Android LMK process termination
    if (currentRssMb >= config.maxRssMb) {
      logger.warn('memory_circuit_breaker_tripped', 'Threshold exceeded; shedding incoming request', {
        path: req.path,
        method: req.method,
        clientIp: req.ip,
        metadata: {
          currentRssMb: Math.round(currentRssMb * 10) / 10,
          maxRssMb: config.maxRssMb,
        },
      });

      // Suggest garbage collection if V8 flags allow (--expose-gc)
      if (typeof global.gc === 'function') {
        try {
          global.gc();
        } catch {
          // Ignore GC invocation failure
        }
      }

      res.setHeader('Retry-After', config.retryAfterSec.toString());
      res.status(503).json({
        type: 'https://tools.ietf.org/html/rfc7231#section-6.6.4',
        title: 'Service Temporarily Unavailable',
        status: 503,
        detail:
          'Server memory circuit breaker activated to protect local device stability. Please retry shortly.',
        retryAfterSeconds: config.retryAfterSec,
      });
      return;
    }

    // Soft Limit Warning: Log internal telemetry for proactive capacity monitoring
    if (currentRssMb >= config.warnRssMb) {
      logger.debug('memory_pressure_high', 'Server is approaching memory limit', {
        path: req.path,
        metadata: {
          currentRssMb: Math.round(currentRssMb * 10) / 10,
          warnRssMb: config.warnRssMb,
        },
      });
    }

    next();
  };
}
