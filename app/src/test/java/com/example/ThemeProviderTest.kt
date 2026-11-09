package com.example

import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.ThemeController
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ThemeProviderTest {

    @Test
    fun testDefaultThemeModeIsSystem() {
        val context = RuntimeEnvironment.getApplication()
        val controller = ThemeController.create(context)
        assertEquals(AppThemeMode.SYSTEM, controller.themeMode.value)
    }

    @Test
    fun testSwitchingToDarkModeAndPersistence() {
        val context = RuntimeEnvironment.getApplication()
        val controller = ThemeController.create(context)

        controller.setThemeMode(AppThemeMode.DARK)
        assertEquals(AppThemeMode.DARK, controller.themeMode.value)

        // Verify persistence by creating a second controller with the same context
        val secondController = ThemeController.create(context)
        assertEquals(AppThemeMode.DARK, secondController.themeMode.value)
    }

    @Test
    fun testSwitchingToLightModeAndPersistence() {
        val context = RuntimeEnvironment.getApplication()
        val controller = ThemeController.create(context)

        controller.setThemeMode(AppThemeMode.LIGHT)
        assertEquals(AppThemeMode.LIGHT, controller.themeMode.value)

        val secondController = ThemeController.create(context)
        assertEquals(AppThemeMode.LIGHT, secondController.themeMode.value)
    }

    @Test
    fun testAppThemeModeIsDarkEvaluation() {
        // Light mode is always false
        assertFalse(AppThemeMode.LIGHT.shouldApplyDark(systemInDark = true))
        assertFalse(AppThemeMode.LIGHT.shouldApplyDark(systemInDark = false))

        // Dark mode is always true
        assertTrue(AppThemeMode.DARK.shouldApplyDark(systemInDark = false))
        assertTrue(AppThemeMode.DARK.shouldApplyDark(systemInDark = true))

        // System mode delegates to systemInDark parameter
        assertTrue(AppThemeMode.SYSTEM.shouldApplyDark(systemInDark = true))
        assertFalse(AppThemeMode.SYSTEM.shouldApplyDark(systemInDark = false))
    }
}
