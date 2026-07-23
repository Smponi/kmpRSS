package com.smponi.reader.core.designsystem

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

@Composable
internal actual fun platformAccessibilityPreferences(): AccessibilityPreferences {
    val context = LocalContext.current
    val accessibilityManager = context.getSystemService(AccessibilityManager::class.java)
    val motionScale = Settings.Global.getFloat(
        context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f,
    )
    return AccessibilityPreferences(
        reduceMotion = motionScale == 0f,
        increaseContrast = accessibilityManager?.readHighContrastPreference() == true,
        fontScale = LocalDensity.current.fontScale,
    )
}

private const val HIGH_CONTRAST_TEXT_API_LEVEL = 36
private const val LEGACY_HIGH_CONTRAST_TEXT_METHOD = "isHighTextContrastEnabled"

/**
 * Selects the framework API without linking the API 36 method on older Android releases.
 *
 * Android renamed the public high-text-contrast accessor in API 36. The legacy method is
 * therefore resolved reflectively on API 30–35, where it is available at runtime but absent
 * from the API 36 compile SDK.
 */
internal fun readHighContrastPreference(
    sdkInt: Int,
    api36Reader: () -> Boolean,
    legacyReader: () -> Boolean,
): Boolean = runCatching {
    if (sdkInt >= HIGH_CONTRAST_TEXT_API_LEVEL) {
        api36Reader()
    } else {
        legacyReader()
    }
}.getOrDefault(false)

private fun AccessibilityManager.readHighContrastPreference(): Boolean = readHighContrastPreference(
    sdkInt = Build.VERSION.SDK_INT,
    api36Reader = ::readApi36HighContrastPreference,
    legacyReader = ::readLegacyHighContrastPreference,
)

@SuppressLint("NewApi")
private fun AccessibilityManager.readApi36HighContrastPreference(): Boolean = isHighContrastTextEnabled

private fun AccessibilityManager.readLegacyHighContrastPreference(): Boolean =
    javaClass.getMethod(LEGACY_HIGH_CONTRAST_TEXT_METHOD).invoke(this) as? Boolean ?: false

@Composable
internal actual fun PlatformReaderTheme(accessibility: DesignAccessibilityPolicy, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        val designSystem = ReaderDesignSystem(
            colors = ReaderColors(
                surface = colorScheme.surface,
                onSurface = colorScheme.onSurface,
                surfaceContainer = colorScheme.surfaceContainer,
                onSurfaceVariant = colorScheme.onSurfaceVariant,
                accent = colorScheme.primary,
                onAccent = colorScheme.onPrimary,
                outline = colorScheme.outline,
                error = colorScheme.error,
            ),
            typography = ReaderTypography(
                display = MaterialTheme.typography.displaySmall,
                title = MaterialTheme.typography.titleLarge,
                body = MaterialTheme.typography.bodyLarge,
                label = MaterialTheme.typography.labelLarge,
            ),
            accessibility = accessibility,
        )
        CompositionLocalProvider(LocalReaderDesignSystem provides designSystem, content = content)
    }
}
