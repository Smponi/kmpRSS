package com.smponi.reader.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.smponi.reader.feature.home.HomeOutcome
import com.smponi.reader.feature.onboarding.OnboardingOutcome
import kotlinx.serialization.Serializable

/**
 * Stable identity for every application destination.
 *
 * Keys contain restoration identity only. Feature state, callbacks and localized copy stay outside the stack.
 */
@Serializable
sealed interface AppNavKey : NavKey {
    /** The one-screen onboarding and website-entry destination. */
    @Serializable
    data object Onboarding : AppNavKey

    /** The data-free Home shell. */
    @Serializable
    data object Home : AppNavKey

    /** Feed discovery for the website explicitly submitted by the person. */
    @Serializable
    data class FeedDiscovery(val website: String) : AppNavKey
}

/** Typed feature handoffs accepted by the application navigation runtime. */
sealed interface AppNavigationEvent {
    data class Onboarding(val outcome: OnboardingOutcome) : AppNavigationEvent

    data class Home(val outcome: HomeOutcome) : AppNavigationEvent

    data object EditWebsite : AppNavigationEvent
}

/**
 * Authoritative Navigation 3 runtime shared by Android and iOS.
 *
 * The public [backStack] is read-only to callers; all mutations stay centralized in [handle].
 */
class AppNavigation(initialBackStack: List<AppNavKey> = listOf(AppNavKey.Onboarding)) {
    init {
        require(initialBackStack.isNotEmpty()) {
            "App navigation requires a root destination."
        }
    }

    internal val navigation3BackStack = NavBackStack(*initialBackStack.toTypedArray())

    val backStack: List<AppNavKey>
        get() = navigation3BackStack

    /** Applies one feature handoff using the deterministic application stack rules. */
    fun handle(event: AppNavigationEvent) {
        when (event) {
            is AppNavigationEvent.Home -> when (event.outcome) {
                HomeOutcome.FollowWebsite -> navigation3BackStack += AppNavKey.Onboarding
            }

            is AppNavigationEvent.Onboarding -> when (val outcome = event.outcome) {
                is OnboardingOutcome.FollowWebsite ->
                    navigation3BackStack += AppNavKey.FeedDiscovery(outcome.website)

                OnboardingOutcome.UseApp -> returnToHomeWithoutDuplicate()
            }

            AppNavigationEvent.EditWebsite -> {
                if (navigation3BackStack.lastOrNull() is AppNavKey.FeedDiscovery) {
                    navigation3BackStack.removeLast()
                }
            }
        }
    }

    private fun returnToHomeWithoutDuplicate() {
        val homeIndex = navigation3BackStack.indexOfLast { it == AppNavKey.Home }
        if (homeIndex < 0) {
            navigation3BackStack.clear()
            navigation3BackStack += AppNavKey.Home
        } else {
            navigation3BackStack.subList(homeIndex + 1, navigation3BackStack.size).clear()
        }
    }
}
