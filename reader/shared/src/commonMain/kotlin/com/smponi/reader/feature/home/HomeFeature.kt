package com.smponi.reader.feature.home

import androidx.compose.runtime.Composable

/**
 * Connects the public Home session to a platform-owned screen.
 *
 * A later integration slice owns where [HomeOutcome.FollowWebsite] leads. This feature never receives a navigator.
 */
@Composable
fun HomeFeature(home: Home, onOutcome: (HomeOutcome) -> Unit) {
    PlatformHomeScreen(
        state = home.state,
        onAction = { action -> onOutcome(home.onAction(action)) },
    )
}

/** Platform rendering seam: both platforms share empty-state meaning, not visual structure or component chrome. */
@Composable
internal expect fun PlatformHomeScreen(state: HomeState, onAction: (HomeAction) -> Unit)
