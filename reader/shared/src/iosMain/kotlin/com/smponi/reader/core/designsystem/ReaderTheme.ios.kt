package com.smponi.reader.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import platform.UIKit.UIAccessibilityDarkerSystemColorsEnabled
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled
import platform.UIKit.UIAccessibilityIsReduceTransparencyEnabled

@Composable
internal actual fun platformAccessibilityPreferences() = AccessibilityPreferences(
    reduceMotion = UIAccessibilityIsReduceMotionEnabled(),
    reduceTransparency = UIAccessibilityIsReduceTransparencyEnabled(),
    increaseContrast = UIAccessibilityDarkerSystemColorsEnabled(),
    fontScale = LocalDensity.current.fontScale,
)

@Composable
internal actual fun PlatformReaderTheme(accessibility: DesignAccessibilityPolicy, content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) AppleDarkColors else AppleLightColors
    val designSystem = ReaderDesignSystem(
        colors = colors,
        typography = AppleTypography,
        accessibility = accessibility,
    )
    CompositionLocalProvider(LocalReaderDesignSystem provides designSystem, content = content)
}

private val AppleLightColors = ReaderColors(
    surface = Color(0xFFF7F7F8),
    onSurface = Color(0xFF171719),
    surfaceContainer = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFF515157),
    accent = Color(0xFF006B55),
    onAccent = Color.White,
    outline = Color(0xFF74747A),
    error = Color(0xFFB42318),
)

private val AppleDarkColors = ReaderColors(
    surface = Color(0xFF000000),
    onSurface = Color(0xFFF5F5F7),
    surfaceContainer = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFFC7C7CC),
    accent = Color(0xFF63D9BC),
    onAccent = Color(0xFF002118),
    outline = Color(0xFF8E8E93),
    error = Color(0xFFFFB4AB),
)

private val AppleTypography = ReaderTypography(
    display = TextStyle(fontSize = 34.sp, lineHeight = 41.sp, fontWeight = FontWeight.Bold),
    title = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold),
    body = TextStyle(fontSize = 17.sp, lineHeight = 22.sp),
    label = TextStyle(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
)
