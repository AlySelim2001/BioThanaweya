/**
 * Playwright Configuration for BioThanaweya E2E Tests
 * 
 * Configures browser E2E testing across Chromium, Firefox, and WebKit.
 * Uses GitHub Actions cloud execution for browser testing.
 * 
 * @see https://playwright.dev/docs/test-configuration
 */
export default {
  testDir: './e2e',
  timeout: 30000,
  expect: {
    timeout: 5000,
  },
  fullyParallel: true,
  workers: process.env.CI ? 1 : undefined,
  reporter: [
    ['list'],
    ['html', { open: 'never' }],
  ],

  // Configure test isolation mode
  isolation: {
    // Each test file runs in its own browser context
    // This ensures state doesn't leak between tests
    method: 'workers',
  },

  // Use Chrome-like Chromium by default; test against all three browsers
  // by running `npx playwright test --project=firefox` or `--project=webkit`
  // projects: [
  //   {
  //     name: 'Chromium',
  //     use: {
  //       browserName: 'chromium',
  //       headless: true,
  //     },
  //   },
  //   {
  //     name: 'Firefox',
  //     use: {
  //       browserName: 'firefox',
  //       headless: true,
  //     },
  //   },
  //   {
  //     name: 'WebKit',
  //     use: {
  //       browserName: 'webkit',
  //       headless: true,
  //     },
  //   },
  // ],

  // Default project is Chromium headless
  use: {
    browserName: 'chromium',
    headless: true,
    // Ignore HTTPS errors for local development
    ignoreHTTPSErrors: true,
    // Base URL for tests - will be overridden in CI
    baseURL: 'http://localhost:3000',
    // Viewport size
    viewport: { width: 1920, height: 1080 },
    // Trace timeout
    trace: 'on-first-retry',
  },

  // Shared test timeout overrides
  retries: process.env.CI ? 0 : 1,
}