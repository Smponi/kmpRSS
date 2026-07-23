package com.smponi.reader.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

/** Android Navigation 3 display with platform back and predictive-back integration. */
@Composable
internal actual fun PlatformAppNavigationHost(navigation: AppNavigation, content: @Composable (AppNavKey) -> Unit) {
    NavDisplay(
        backStack = navigation.navigation3BackStack,
        onBack = {
            navigation.handle(AppNavigationEvent.Back)
        },
        entryProvider = { key ->
            NavEntry(key) {
                content(key)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}
