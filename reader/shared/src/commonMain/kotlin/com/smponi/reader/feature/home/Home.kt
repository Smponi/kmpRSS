package com.smponi.reader.feature.home

/** Observable content state of the Home feature. No subscription data exists in this slice. */
sealed interface HomeState {
    /** Inbox, Saved, tags and feeds all contain no items. */
    data object Empty : HomeState
}

/** User intent accepted by [Home]. */
sealed interface HomeAction {
    /** Begin the existing website-following journey without owning its navigation. */
    data object FollowWebsite : HomeAction
}

/** Meaningful handoffs emitted by [Home] for the app caller to integrate later. */
sealed interface HomeOutcome {
    /** The caller should open the website-following journey. */
    data object FollowWebsite : HomeOutcome
}

/**
 * Small public Home-feature interface shared by the app caller and tests.
 *
 * This shell owns no navigation, feed data, persistence or invented content.
 */
class Home internal constructor() {
    val state: HomeState = HomeState.Empty

    /** Converts the only executable shell action into an honest caller-owned handoff. */
    fun onAction(action: HomeAction): HomeOutcome = when (action) {
        HomeAction.FollowWebsite -> HomeOutcome.FollowWebsite
    }
}

/** Opens the data-free Home shell in its truthful empty state. */
fun openHome(): Home = Home()
