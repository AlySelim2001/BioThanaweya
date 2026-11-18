import { describe, it, expect } from 'vitest';
import { globalErrorHandler } from '../security';

const createMockRes = (): any => {
  const res: any = {
    status: (code: number) => {
      ;(res as { statusCode: number }).statusCode = code;
      return res;
    },
    setHeader: (_key: string, _value: string) => {
    },
    json: (_data: any) => {
    },
  };
  return res;
};

const mockReq = (): any => ({
  ip: '127.0.0.1',
  socket: { remoteAddress: '127.0.0.1' },
});

describe('Global Error Handler', () => {
  it('handles generic error without throwing', () => {
    const req = mockReq();
    const res = createMockRes();
    const next = () => {};

    expect(() =>
      globalErrorHandler(new Error('Test error'), req, res, next)
    ).not.toThrow();

    expect((res as { statusCode: number }).statusCode).toBe(500);
  });

  it('routes non-Zod errors to default error handler', () => {
    const req = mockReq();
    const res = createMockRes();
    const next = () => {};

    globalErrorHandler({ message: 'Custom error' } as any, req, res, next);

    expect(() => globalErrorHandler({ message: 'Custom error' } as any, req, res, next))
      .not.toThrow();

    expect((res as { statusCode: number }).statusCode).toBe(500);
  });
});