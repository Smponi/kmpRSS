package com.smponi.reader.feature.discovery

import com.smponi.reader.feature.onboarding.OnboardingOutcome

/** Observable feed-discovery progress for one website handed off by onboarding. */
sealed interface FeedDiscoveryState {
    val website: String

    /** The website is ready for its feed links to be discovered. */
    data class Discovering(override val website: String) : FeedDiscoveryState
}

/**
 * Public feature session shared by callers and tests.
 *
 * Network discovery and platform rendering remain implementation details behind this state interface.
 */
class FeedDiscovery internal constructor(initialState: FeedDiscoveryState) {
    var state: FeedDiscoveryState = initialState
        internal set
}

/** Starts discovery immediately from the meaningful onboarding handoff. */
fun beginFeedDiscovery(outcome: OnboardingOutcome.FollowWebsite): FeedDiscovery {
    val website = outcome.website.withWebScheme()
    return FeedDiscovery(FeedDiscoveryState.Discovering(website))
}

private fun String.withWebScheme(): String = when {
    startsWith("https://", ignoreCase = true) -> this
    startsWith("http://", ignoreCase = true) -> this
    else -> "https://$this"
}
