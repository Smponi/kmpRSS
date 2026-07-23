package com.smponi.reader.feature.discovery

import com.smponi.reader.feature.onboarding.OnboardingOutcome
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedDiscoveryTest {
    @Test
    fun `normal website begins feed discovery immediately`() {
        val discovery = beginFeedDiscovery(
            OnboardingOutcome.FollowWebsite("example.com"),
        )

        assertEquals(
            FeedDiscoveryState.Discovering("https://example.com"),
            discovery.state,
        )
    }
}
