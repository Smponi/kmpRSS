package com.smponi.reader.feature.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.smponi.reader.feature.onboarding.OnboardingOutcome
import com.smponi.reader.feature.preview.FeedPreviewFeature
import com.smponi.reader.feature.preview.FeedPreviewState
import com.smponi.reader.feature.preview.openFeedPreview

/**
 * Connects one onboarding website handoff to real discovery and a platform-owned screen.
 *
 * Retry stays inside this feature. Returning to website entry remains owned by the caller and does not require a
 * navigation framework.
 */
@Composable
fun FeedDiscoveryFeature(
    outcome: OnboardingOutcome.FollowWebsite,
    onCandidateSelected: (FeedDiscoveryOutcome.CandidateSelected) -> Unit,
    onEditWebsite: () -> Unit,
) {
    val discovery = remember(outcome) { beginFeedDiscovery(outcome) }
    var state by remember(discovery) { mutableStateOf(discovery.state) }
    var attempt by remember(discovery) { mutableIntStateOf(0) }
    var preview by remember(discovery) { mutableStateOf<FeedPreviewState?>(null) }

    DisposableEffect(discovery) {
        onDispose(discovery::close)
    }
    LaunchedEffect(discovery, attempt) {
        state = FeedDiscoveryState.Discovering(discovery.state.website)
        discovery.discover()
        state = discovery.state
    }

    val currentPreview = preview
    if (currentPreview == null) {
        PlatformFeedDiscoveryScreen(
            state = state,
            onCandidateSelected = { candidate ->
                discovery.select(candidate)?.let { selection ->
                    state = discovery.state
                    onCandidateSelected(selection)
                    preview = openFeedPreview(selection)
                }
            },
            onRetry = { attempt += 1 },
            onEditWebsite = onEditWebsite,
        )
    } else {
        FeedPreviewFeature(
            state = currentPreview,
            onEditSelection = { preview = null },
        )
    }
}

/** Platform rendering seam: the discovery meaning is shared while component chrome remains native to each platform. */
@Composable
internal expect fun PlatformFeedDiscoveryScreen(
    state: FeedDiscoveryState,
    onCandidateSelected: (FeedCandidate) -> Unit,
    onRetry: () -> Unit,
    onEditWebsite: () -> Unit,
)
