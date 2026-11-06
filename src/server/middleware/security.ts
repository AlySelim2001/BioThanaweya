import { Request, Response, NextFunction } from 'express';
import { ZodError } from 'zod';

// Security Headers Middleware
export function securityHeaders(_req: Request, res: Response, next: NextFunction): void {
  res.setHeader('X-Content-Type-Options', 'nosniff');
  res.setHeader('X-Frame-Options', 'SAMEORIGIN');
  res.setHeader('X-XSS-Protection', '1; mode=block');
  res.setHeader('Referrer-Policy', 'strict-origin-when-cross-origin');
  next();
}

// In-Memory Rate Limiter with sliding window and automatic memory pruning (crucial for Termux)
interface RateRecord {
  count: number;
  resetAt: number;
}

export function createRateLimiter(options: { maxRequests: number; windowMs: number }) {
  const store = new Map<string, RateRecord>();

  // Prune expired entries every minute to prevent memory leak on smartphone
  setInterval(() => {
    const now = Date.now();
    for (const [ip, record] of store.entries()) {
      if (now > record.resetAt) {
        store.delete(ip);
      }
    }
  }, 60000).unref();

  return (req: Request, res: Response, next: NextFunction): void => {
    const clientIp = req.ip || req.socket.remoteAddress || '127.0.0.1';
    const now = Date.now();
    const record = store.get(clientIp);

    if (!record || now > record.resetAt) {
      store.set(clientIp, { count: 1, resetAt: now + options.windowMs });
      return next();
    }

    if (record.count >= options.maxRequests) {
      const retryAfterSec = Math.ceil((record.resetAt - now) / 1000);
      res.setHeader('Retry-After', retryAfterSec.toString());
      res.status(429).json({
        type: 'https://tools.ietf.org/html/rfc6585#section-4',
        title: 'Too Many Requests',
        status: 429,
        detail: `Rate limit exceeded. Please wait ${retryAfterSec} seconds before retrying.`,
      });
      return;
    }

    record.count += 1;
    next();
  };
}

// RFC 7807 Centralized Error Handler Middleware
export function globalErrorHandler(
  err: any,
  _req: Request,
  res: Response,
  _next: NextFunction
): void {
  if (err instanceof ZodError) {
    res.status(400).json({
      type: 'https://tools.ietf.org/html/rfc7231#section-6.5.1',
      title: 'Bad Request - Validation Error',
      status: 400,
      detail: 'The submitted request payload failed strict schema validation.',
      errors: err.errors.map((e: any) => ({
        path: Array.isArray(e.path) ? e.path.join('.') : String(e.path),
        message: e.message,
      })),
    });
    return;
  }

  const statusCode = err.status || err.statusCode || 500;
  const isProd = process.env.NODE_ENV === 'production';

  res.status(statusCode).json({
    type: 'https://tools.ietf.org/html/rfc7231#section-6.6.1',
    title: err.name || 'Internal Server Error',
    status: statusCode,
    detail: isProd ? 'An unexpected system error occurred.' : err.message || 'Unknown error',
  });
}
