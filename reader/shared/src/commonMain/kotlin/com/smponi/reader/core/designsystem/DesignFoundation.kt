package com.smponi.reader.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/** System accessibility preferences that can change the presentation without changing product behaviour. */
@Immutable
data class AccessibilityPreferences(
    val reduceMotion: Boolean = false,
    val reduceTransparency: Boolean = false,
    val increaseContrast: Boolean = false,
    val fontScale: Float = 1f,
)

enum class MotionLevel {
    Full,
    Reduced,
}

enum class ContrastLevel {
    Standard,
    High,
}

/** Pure policy seam used by both platform theme adapters and by tests. */
@Immutable
data class DesignAccessibilityPolicy(
    val motionLevel: MotionLevel,
    val contrastLevel: ContrastLevel,
    val usesTransparency: Boolean,
) {
    companion object {
        fun from(preferences: AccessibilityPreferences) = DesignAccessibilityPolicy(
            motionLevel = if (preferences.reduceMotion) MotionLevel.Reduced else MotionLevel.Full,
            contrastLevel = if (preferences.increaseContrast) ContrastLevel.High else ContrastLevel.Standard,
            usesTransparency = !preferences.reduceTransparency,
        )
    }
}

/** Cross-platform spacing intent. Component shape and visual styling remain owned by each platform adapter. */
object FoundationSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
}

object FoundationSize {
    /** Android and iOS controls must expose at least a 48dp interactive region. */
    val minimumInteractiveTarget = 48.dp
}

@Immutable
data class ReaderColors(
    val surface: Color,
    val onSurface: Color,
    val surfaceContainer: Color,
    val onSurfaceVariant: Color,
    val accent: Color,
    val onAccent: Color,
    val outline: Color,
    val error: Color,
)

@Immutable
data class ReaderTypography(val display: TextStyle, val title: TextStyle, val body: TextStyle, val label: TextStyle)

@Immutable
data class ReaderDesignSystem(
    val colors: ReaderColors,
    val typography: ReaderTypography,
    val accessibility: DesignAccessibilityPolicy,
)

val LocalReaderDesignSystem = staticCompositionLocalOf<ReaderDesignSystem> {
    error("ReaderTheme must wrap platform UI")
}
