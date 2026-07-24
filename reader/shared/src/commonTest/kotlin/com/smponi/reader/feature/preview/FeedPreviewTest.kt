package com.smponi.reader.feature.preview

import com.smponi.reader.feature.discovery.FeedCandidate
import com.smponi.reader.feature.discovery.FeedDiscoveryOutcome
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedPreviewTest {
    @Test
    fun `candidate handoff becomes preview without inventing article data`() {
        val outcome = FeedDiscoveryOutcome.CandidateSelected(
            website = "https://example.com",
            candidate = FeedCandidate(
                title = "Example updates",
                url = "https://example.com/feed.xml",
            ),
        )

        val preview = openFeedPreview(outcome)

        assertEquals(
            FeedPreviewState(
                website = "https://example.com",
                candidate = FeedCandidate(
                    title = "Example updates",
                    url = "https://example.com/feed.xml",
                ),
            ),
            preview,
        )
    }
}
