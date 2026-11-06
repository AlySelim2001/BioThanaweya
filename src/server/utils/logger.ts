/**
 * @module src/server/utils/logger.ts
 * @description Zero-dependency, ultra-low-overhead Structured JSON (NDJSON) Logger.
 * Specifically architected for resource-constrained environments (e.g., Termux on Android)
 * to avoid the memory footprint of bulky logging frameworks while providing machine-parseable telemetry.
 */

export type LogLevel = 'DEBUG' | 'INFO' | 'WARN' | 'ERROR';

export interface StructuredLogPayload {
  timestamp: string;
  level: LogLevel;
  event: string;
  message?: string;
  durationMs?: number;
  statusCode?: number;
  path?: string;
  method?: string;
  clientIp?: string;
  rssMb: number;
  heapUsedMb: number;
  metadata?: Record<string, unknown>;
  error?: {
    name: string;
    message: string;
    stack?: string;
  };
}

class JsonLogger {
  private readonly isProduction = process.env.NODE_ENV === 'production';

  private getMemoryMetrics(): { rssMb: number; heapUsedMb: number } {
    const mem = process.memoryUsage();
    return {
      rssMb: Math.round((mem.rss / 1024 / 1024) * 10) / 10,
      heapUsedMb: Math.round((mem.heapUsed / 1024 / 1024) * 10) / 10,
    };
  }

  private write(
    level: LogLevel,
    event: string,
    message?: string,
    extra?: Partial<StructuredLogPayload>
  ): void {
    const mem = this.getMemoryMetrics();
    const entry: StructuredLogPayload = {
      timestamp: new Date().toISOString(),
      level,
      event,
      ...(message ? { message } : {}),
      rssMb: mem.rssMb,
      heapUsedMb: mem.heapUsedMb,
      ...extra,
    };

    const output = JSON.stringify(entry);

    if (level === 'ERROR') {
      process.stderr.write(output + '\n');
    } else {
      process.stdout.write(output + '\n');
    }
  }

  public debug(event: string, message?: string, extra?: Partial<StructuredLogPayload>): void {
    if (!this.isProduction) {
      this.write('DEBUG', event, message, extra);
    }
  }

  public info(event: string, message?: string, extra?: Partial<StructuredLogPayload>): void {
    this.write('INFO', event, message, extra);
  }

  public warn(event: string, message?: string, extra?: Partial<StructuredLogPayload>): void {
    this.write('WARN', event, message, extra);
  }

  public error(
    event: string,
    err: unknown,
    message?: string,
    extra?: Partial<StructuredLogPayload>
  ): void {
    const errorDetails =
      err instanceof Error
        ? {
            name: err.name,
            message: err.message,
            stack: this.isProduction ? undefined : err.stack,
          }
        : {
            name: 'UnknownError',
            message: String(err),
          };

    this.write('ERROR', event, message, {
      ...extra,
      error: errorDetails,
    });
  }
}

export const logger = new JsonLogger();
