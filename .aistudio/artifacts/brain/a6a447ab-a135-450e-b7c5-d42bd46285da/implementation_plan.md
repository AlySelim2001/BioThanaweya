# Refactoring & Hardening Blueprint: `server.ts` Pipeline

A comprehensive Clean Code 2026 refactoring plan for the BioThanaweya server entry point and HTTP pipeline, implementing structured JSON logging, memory-aware circuit breaking, and aggressive resource tuning for smartphone Termux environments.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> Confirmed architecture specifications from Phase 1:
> - **Target Module**: `server.ts` entry point and HTTP pipeline.
> - **Logging & Observability**: High-efficiency, zero-dependency structured JSON (NDJSON) logging with live RSS memory context.
> - **Fault Tolerance**: Proactive Memory Circuit Breaker protecting against Android Low Memory Killer (LMK) crashes by shedding load when heap/RSS exceeds threshold.
> - **Target Platform**: Low-memory Termux smartphone container runtime (ARM64, ≤ 128MB RAM budget).

- **Confirmed Decision 1**: Implement an asynchronous, memory-monitoring pipeline interceptor that trips when RSS exceeds 110MB, returning RFC 7807 503 responses until memory recedes.
- **Confirmed Decision 2**: Replace unstructured `console.log` with a structured JSON logger format (`{"time":"...", "level":"...", "msg":"...", "rssMb":...}`).
- **Confirmed Decision 3**: Optimize TCP socket pooling with shortened keep-alive timeouts (5000ms) and aggressive idle cleanup to preserve socket descriptors in Android shells.

---

## 1. Overview & Core Concept

- **What It Does**: Transforms the monolithic Express server into a clean, resilient, memory-governed HTTP pipeline that runs reliably on resource-constrained Android devices (via Termux/PRoot) without thermal throttling or out-of-memory terminates.
- **Target Audience**: Students and self-hosters deploying local BioThanaweya engines on mobile devices with zero cloud dependency.
- **Key Value**: 100% crash resilience against low memory conditions, defensive schema enforcement, and sub-10ms request turnaround.

---

## 2. Technical Architecture & Pipeline Flow

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Incoming HTTP Request (Port 3000)               │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 1: Security Headers & CORS                 │
│            (X-Content-Type-Options, X-Frame-Options, Strict Origin)     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 2: Memory Circuit Breaker                 │
│       Is process.memoryUsage().rss > 110MB?                            │
│       ├── YES ──► Reject with 503 Service Unavailable + Retry-After    │
│       └── NO  ──► Proceed to Stage 3                                   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 3: Sliding-Window Rate Limiter            │
│       (Max 30 req/min per IP, auto-purging memory map every 60s)       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 4: Strict Payload Parsing                 │
│       (Express JSON parser capped at 100kb; drops oversized payloads)  │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 5: Zod Schema Validation & Routing        │
│       ├── /api/health           ──► Returns Termux resource metrics    │
│       ├── /api/examiner/grade   ──► Validates DTO -> AiExaminerService │
│       └── /api/educator/explain ──► Validates DTO -> AiExaminerService │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│               Pipeline Stage 6: Centralized RFC 7807 Error Handler     │
│       (Catches Zod errors, internal exceptions; logs JSON telemetry)   │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Key Architectural Decisions & Clean Code Standards

### 1. Structured JSON Logger (`logger.ts`)
- *Pattern*: Lightweight logging singleton without heavy third-party packages (Pino/Winston) that add bundle weight and memory overhead.
- *Format*:
  ```json
  {"timestamp":"2026-11-05T08:30:00.000Z","level":"INFO","event":"request_completed","path":"/api/examiner/grade","status":200,"durationMs":14.2,"rssMb":42.5}
  ```

### 2. Proactive Memory Circuit Breaker
- *Mechanism*: Middleware checks `process.memoryUsage().rss` on every request.
- *Thresholds*: Soft alert at 90MB RSS, Hard trip at 115MB RSS.
- *Action*: When tripped, sends HTTP 503 (`Retry-After: 5`) and triggers immediate pruning of rate-limiter maps, allowing the Node.js V8 garbage collector to reclaim heap before Android kills the process.

### 3. Graceful Socket & Process Lifecycle
- *Handlers*: Attaches listeners for `SIGINT`, `SIGTERM`, `SIGHUP`, and `uncaughtException`.
- *Teardown*: Calls `server.close()`, sets socket idle timeouts to 1000ms, and terminates within a 3-second deadline to prevent orphaned background processes in Termux.

---

## 4. Implementation Steps

1. Create `src/server/utils/logger.ts`:
   - Pure TypeScript structured NDJSON logger with log levels (`DEBUG`, `INFO`, `WARN`, `ERROR`), automatic timestamping, and runtime memory RSS sampling.
2. Create `src/server/middleware/circuitBreaker.ts`:
   - Memory threshold monitor with configurable limits and RFC 7807 503 response builder.
3. Refactor `server.ts`:
   - Integrate the structured logger and memory circuit breaker into the Express pipeline.
   - Enforce explicit type signatures across all middleware and route handlers.
   - Configure socket timeouts for low-power mobile network environments.
4. Verification & Validation:
   - Co-execute `compile_applet` and `lint_applet`.
   - Verify `GET /api/health` and test circuit breaker telemetry under simulated conditions.
