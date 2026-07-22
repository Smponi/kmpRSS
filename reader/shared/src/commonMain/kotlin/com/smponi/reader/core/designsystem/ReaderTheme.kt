package com.smponi.reader.core.designsystem

import androidx.compose.runtime.Composable

/**
 * Public theme seam for feature UI. The actual visual language is selected by the platform adapter.
 * Android may expose Material 3 to Android-only UI; iOS never receives a Material theme from this interface.
 */
@Composable
fun ReaderTheme(content: @Composable () -> Unit) {
    val preferences = platformAccessibilityPreferences()
    PlatformReaderTheme(
        accessibility = DesignAccessibilityPolicy.from(preferences),
        content = content,
    )
}

@Composable
internal expect fun platformAccessibilityPreferences(): AccessibilityPreferences

@Composable
internal expect fun PlatformReaderTheme(accessibility: DesignAccessibilityPolicy, content: @Composable () -> Unit)
