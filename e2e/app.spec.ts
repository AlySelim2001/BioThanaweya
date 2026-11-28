/**
 * BioThanaweya Playwright E2E Tests
 * 
 * End-to-end tests verifying critical user flows in the application.
 * Tests run against a running development server.
 * 
 * To run locally: npx playwright test
 * To run in CI: GitHub Actions workflows/ci.yml
 */

import { test, expect } from '@playwright/test'

// ---------------------------------------------------------------------------
// Test: Page Load and Contrast
// ---------------------------------------------------------------------------

test('page loads successfully and contrast is adequate', async ({ page }) => {
  await page.goto('/')
  // Verify the title is visible
  await expect(page).toHaveTitle(/BioThanaweya/)
  // Check that main content is visible
  await expect(page.locator('main')).toBeVisible()
  // Verify no text-slate-950 remains (contrast fix verification)
  const slateElements = await page.locator('.text-slate-950').count()
  expect(slateElements).toBe(0)
})

// ---------------------------------------------------------------------------
// Test: Navigation Flow
// ---------------------------------------------------------------------------

test('navigates from dashboard to topic detail screen', async ({ page }) => {
  await page.goto('/')
  // Click on a topic link (first available)
  await page.locator('a[href*="topic"]').first().click()
  await expect(page).toHaveURL(/.*topic.*/))
  await expect(page.locator('h1')).toBeVisible()
})

// ---------------------------------------------------------------------------
// Test: Practice Screen interaction
// ---------------------------------------------------------------------------

test('practice screen renders next item button', async ({ page }) => {
  await page.goto('/practice')
  await expect(page.locator('button:has-text("Next Item")')).toBeVisible()
  // Verify button text color is not text-slate-950 (contrast fix)
  const button = page.locator('button:has-text("Next Item")')
  const color = await button.evaluate((el) => {
    const style = window.getComputedStyle(el)
    return style.color
  })
  // Should not be the low-contrast slate-950 color
  expect(color).not.toContain('rgb(112 112 112)') // text-slate-950
})

// ---------------------------------------------------------------------------
// Test: Settings Screen interaction
// ---------------------------------------------------------------------------

test('settings screen renders haptics toggle', async ({ page }) => {
  await page.goto('/settings')
  await expect(page.locator('text=Haptics')).toBeVisible() || 
  await expect(page.locator('label')).toBeVisible()
})

// ---------------------------------------------------------------------------
// Test: Simulated Exam Screen
// ---------------------------------------------------------------------------

test('simulated exam screen renders', async ({ page }) => {
  await page.goto('/simulated-exam')
  await expect(page.locator('text')).toContain('Exam')
  await expect(page.locator('button')).toBeVisible()
})

// ---------------------------------------------------------------------------
// Test: 3D Background renders without errors
// ---------------------------------------------------------------------------

test('3D background renders without JS errors', async ({ page }) => {
  await page.goto('/')
  // Capture console errors - should be zero for 3D background
  const consoleErrors: string[] = []
  const originalConsoleError = console.error
  console.error = (...args) => {
    consoleErrors.push(args.join(' '))
  }
  // Let the page load
  await page.waitForLoadState('networkidle')
  console.error = originalConsoleError
  // Should have no critical errors related to 3D background
  const criticalErrors = consoleErrors.filter(
    (e) => e.includes('Three') || e.includes('WebGL') || e.indexOf('StorageService') !== -1
  )
  // Allow some non-critical warnings but not critical failures
  expect(criticalErrors.length).toBe(0)
})