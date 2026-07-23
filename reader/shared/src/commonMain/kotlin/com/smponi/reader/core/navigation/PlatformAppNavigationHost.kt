package com.smponi.reader.core.navigation

import androidx.compose.runtime.Composable

/**
 * Platform display seam for the shared authoritative Navigation 3 runtime.
 *
 * Android owns NavDisplay and system back integration. iOS owns Apple-appropriate presentation without another
 * route model.
 */
@Composable
internal expect fun PlatformAppNavigationHost(navigation: AppNavigation, content: @Composable (AppNavKey) -> Unit)
