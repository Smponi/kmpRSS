package com.smponi.reader.feature.preview

import com.smponi.reader.feature.discovery.FeedCandidate
import com.smponi.reader.feature.discovery.FeedDiscoveryOutcome

/**
 * Stable local evidence available for a read-only feed preview.
 *
 * Article data is deliberately absent until a later feed-loading slice can provide real entries.
 */
data class FeedPreviewState(val website: String, val candidate: FeedCandidate)

/** Consumes the exact candidate-selection handoff without fetching, parsing or persisting anything. */
fun openFeedPreview(outcome: FeedDiscoveryOutcome.CandidateSelected): FeedPreviewState = FeedPreviewState(
    website = outcome.website,
    candidate = outcome.candidate,
)
