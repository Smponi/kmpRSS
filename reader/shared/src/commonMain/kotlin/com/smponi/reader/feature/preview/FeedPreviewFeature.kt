package com.smponi.reader.feature.preview

import androidx.compose.runtime.Composable

/** Renders stable candidate evidence and offers a local return to the discovered choices. */
@Composable
fun FeedPreviewFeature(state: FeedPreviewState, onEditSelection: () -> Unit) {
    PlatformFeedPreviewScreen(
        state = state,
        onEditSelection = onEditSelection,
    )
}

/** Platform rendering seam: preview meaning is shared while Android and iOS own their visual language. */
@Composable
internal expect fun PlatformFeedPreviewScreen(state: FeedPreviewState, onEditSelection: () -> Unit)
