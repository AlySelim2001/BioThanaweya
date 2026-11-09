/**
 * ============================================================================
 * BioThanaweya Full-Stack Server Entry Point (Clean Code 2026 Edition)
 * ============================================================================
 *
 * Radical Architectural Improvements Applied:
 * 1. Clean Architecture Separation: Decoupled HTTP transport from business logic,
 *    delegating domain logic to AiExaminerService and DTO validation to Zod.
 * 2. Proactive Memory Circuit Breaker: Prevents Android OOM (Low Memory Killer) crashes
 *    in mobile Termux environments by shedding requests when RSS memory exceeds threshold.
 * 3. Zero-Overhead Structured NDJSON Logging: Emits machine-readable telemetry with
 *    real-time RAM metrics and sub-millisecond request durations.
 * 4. OWASP Top 10 Hardened Pipeline: Strict security headers, rate limiting with
 *    sliding window memory pruning, input sanitization, and RFC 7807 problem details.
 * 5. Low-Resource Mobile Networking: Tuned socket keep-alive (5000ms) and graceful
 *    signal teardown (SIGINT, SIGTERM, SIGHUP) to prevent orphaned ports in Termux.
 */

import express, { Request, Response, NextFunction } from 'express';
import http from 'http';
import path from 'path';
import { fileURLToPath } from 'url';
import { performance } from 'perf_hooks';

import { config } from './src/server/config/env';
import { GradeExamItemSchema, ExplainConceptSchema } from './src/server/schemas/apiSchemas';
import {
  securityHeaders,
  createRateLimiter,
  globalErrorHandler,
} from './src/server/middleware/security';
import { createMemoryCircuitBreaker } from './src/server/middleware/circuitBreaker';
import { AiExaminerService } from './src/server/services/aiExaminerService';
import { logger } from './src/server/utils/logger';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();

// ----------------------------------------------------------------------------
// Stage 1: Security Headers & Transport Hardening (OWASP A04/A05)
// ----------------------------------------------------------------------------
app.use(securityHeaders);
app.disable('x-powered-by');

// ----------------------------------------------------------------------------
// Stage 2: Request Lifecycle Telemetry & Duration Interceptor
// ----------------------------------------------------------------------------
app.use((req: Request, res: Response, next: NextFunction): void => {
  const startTime = performance.now();

  res.on('finish', () => {
    const durationMs = Math.round((performance.now() - startTime) * 10) / 10;
    // Log all API traffic, suppressing static asset spam
    if (req.path.startsWith('/api')) {
      logger.info('http_request_finished', undefined, {
        method: req.method,
        path: req.path,
        statusCode: res.statusCode,
        durationMs,
        clientIp: req.ip,
      });
    }
  });

  next();
});

// ----------------------------------------------------------------------------
// Stage 3: Proactive Memory Circuit Breaker (Termux / Smartphone Protection)
// Sheds traffic when RSS exceeds budget to protect local smartphone OS stability
app.use(createMemoryCircuitBreaker());

// ----------------------------------------------------------------------------
// Stage 4: Strict Payload Parsing with 100kb Size Ceiling (OWASP A01/A03)
// ----------------------------------------------------------------------------
app.use(express.json({ limit: '100kb' }));

// ----------------------------------------------------------------------------
// Stage 5: In-Memory Sliding Window Rate Limiting (OWASP A01)
// ----------------------------------------------------------------------------
// 30 requests/min for AI endpoints; 120 requests/min for general queries
const aiRateLimiter = createRateLimiter({ maxRequests: 30, windowMs: 60 * 1000 });
const standardRateLimiter = createRateLimiter({ maxRequests: 120, windowMs: 60 * 1000 });

// ----------------------------------------------------------------------------
// Stage 6: Application Routes & Controllers
// ----------------------------------------------------------------------------

/**
 * GET /api/health
 * Diagnostic & Resource Probe for Termux / Low-Resource Hosting.
 * Provides live memory metrics, process uptime, and AI provider status.
 */
app.get('/api/health', (_req: Request, res: Response): void => {
  const mem = process.memoryUsage();
  res.status(200).json({
    status: 'healthy',
    timestamp: new Date().toISOString(),
    uptimeSeconds: Math.floor(process.uptime()),
    architecture: process.arch,
    platform: process.platform,
    nodeVersion: process.version,
    memoryMetrics: {
      rssMb: Math.round((mem.rss / 1024 / 1024) * 10) / 10,
      heapUsedMb: Math.round((mem.heapUsed / 1024 / 1024) * 10) / 10,
      heapTotalMb: Math.round((mem.heapTotal / 1024 / 1024) * 10) / 10,
      externalMb: Math.round((mem.external / 1024 / 1024) * 10) / 10,
    },
    circuitBreaker: {
      maxThresholdMb: process.env.MEMORY_LIMIT_MB
        ? parseInt(process.env.MEMORY_LIMIT_MB, 10)
        : config.isProduction
        ? 128
        : 512,
      isProtected: true,
    },
    aiConfigured: AiExaminerService.isConfigured(),
  });
});

/**
 * POST /api/examiner/grade
 * Evaluates student answers using Egyptian Ministry mark scheme criteria.
 * Enforces Zod validation and prompt injection defenses via AiExaminerService.
 */
app.post(
  '/api/examiner/grade',
  aiRateLimiter,
  async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const validatedInput = GradeExamItemSchema.parse(req.body);
      const assessmentResult = await AiExaminerService.evaluateStudentAnswer(validatedInput);
      res.status(200).json(assessmentResult);
    } catch (error) {
      next(error);
    }
  }
);

/**
 * POST /api/educator/explain
 * Generates technical breakdowns or Arabic ELI5 analogies for curriculum topics.
 */
app.post(
  '/api/educator/explain',
  aiRateLimiter,
  async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const validatedInput = ExplainConceptSchema.parse(req.body);
      const explanationResult = await AiExaminerService.generateExplanation(validatedInput);
      res.status(200).json(explanationResult);
    } catch (error) {
      next(error);
    }
  }
);

// ----------------------------------------------------------------------------
// Stage 7: SPA Static Assets & Dev Middleware
// ----------------------------------------------------------------------------
async function bootstrapServer(): Promise<void> {
  if (config.nodeEnv !== 'production') {
    // Mount Vite middleware in development mode with HMR disabled (environment constraint)
    const { createServer: createViteServer } = await import('vite');
    const vite = await createViteServer({
      server: { middlewareMode: true, hmr: false },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    // Serve optimized production bundle in production mode
    const distPath = path.join(__dirname, 'dist');
    app.use(express.static(distPath, { maxAge: '1d', etag: true }));
    app.get('*', (_req: Request, res: Response) => {
      res.sendFile(path.join(distPath, 'index.html'));
    });
  }

  // --------------------------------------------------------------------------
  // Stage 8: Centralized RFC 7807 Error Handler
  // --------------------------------------------------------------------------
  app.use(globalErrorHandler);

  // --------------------------------------------------------------------------
  // Stage 9: HTTP Server Creation & Low-Power Socket Optimization
  // --------------------------------------------------------------------------
  const server = http.createServer(app);

  // Optimize socket pooling for mobile battery & memory conservation
  server.keepAliveTimeout = 5000; // Close idle keep-alive sockets after 5 seconds
  server.headersTimeout = 6000;   // Prevent slowloris attacks on mobile radios
  server.maxHeadersCount = 50;

  server.on('error', (err: NodeJS.ErrnoException) => {
    logger.error('server_bind_error', err, `Failed to bind HTTP server to 0.0.0.0:${config.port}`, {
      metadata: {
        code: err.code,
        errno: err.errno,
        syscall: err.syscall,
        port: config.port,
      },
    });

    if (err.code === 'EADDRINUSE') {
      logger.error('port_conflict_detected', err, `Port ${config.port} is already in use by an existing process. Run "npm run clean" or execute "fuser -k ${config.port}/tcp" to free the port.`);
    } else if (err.code === 'EACCES') {
      logger.error('port_permission_denied', err, `Insufficient permissions to bind to port ${config.port}. Ensure user has socket bind authorization.`);
    }

    process.exit(1);
  });

  server.listen(config.port, '0.0.0.0', () => {
    logger.info('server_started', `BioThanaweya Server running on http://0.0.0.0:${config.port}`, {
      metadata: {
        port: config.port,
        host: '0.0.0.0',
        env: config.nodeEnv,
        arch: process.arch,
        pid: process.pid,
      },
    });
  });

  // --------------------------------------------------------------------------
  // Stage 10: Graceful Process Termination (Termux / Android Shell Lifecycle)
  // --------------------------------------------------------------------------
  const initiateGracefulShutdown = (signal: string): void => {
    logger.warn('server_shutdown_initiated', `Received ${signal}. Draining connections...`);

    server.close((err?: Error) => {
      if (err) {
        logger.error('server_close_failed', err);
        process.exit(1);
      }
      logger.info('server_shutdown_completed', 'TCP listeners closed cleanly. Exiting.');
      process.exit(0);
    });

    // Forced exit threshold if sockets refuse to disconnect within 4 seconds
    setTimeout(() => {
      logger.error('server_shutdown_timeout', new Error('Forced exit timeout exceeded'));
      process.exit(1);
    }, 4000).unref();
  };

  process.on('SIGTERM', () => initiateGracefulShutdown('SIGTERM'));
  process.on('SIGINT', () => initiateGracefulShutdown('SIGINT'));
  process.on('SIGHUP', () => initiateGracefulShutdown('SIGHUP'));

  // Trap unhandled exceptions to log structured diagnostic before exiting
  process.on('uncaughtException', (err: Error) => {
    logger.error('uncaught_exception_fatal', err, 'Process terminating due to unhandled error');
    process.exit(1);
  });

  process.on('unhandledRejection', (reason: unknown) => {
    logger.error('unhandled_rejection', reason, 'Unhandled Promise rejection intercepted');
  });
}

bootstrapServer().catch((err) => {
  logger.error('bootstrap_failed', err, 'Fatal error during server startup');
  process.exit(1);
});
