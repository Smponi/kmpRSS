package com.smponi.reader.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.smponi.reader.feature.discovery.FeedDiscoveryOutcome
import com.smponi.reader.feature.home.HomeOutcome
import com.smponi.reader.feature.onboarding.OnboardingOutcome
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

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

/**
 * Resolves one key exhaustively without creating a parallel destination model.
 *
 * Platform displays use this provider so adding a key requires updating the single shared mapping.
 */
internal inline fun <T> AppNavKey.provideAppEntry(
    onboarding: () -> T,
    home: () -> T,
    feedDiscovery: (AppNavKey.FeedDiscovery) -> T,
): T = when (this) {
    AppNavKey.Onboarding -> onboarding()
    AppNavKey.Home -> home()
    is AppNavKey.FeedDiscovery -> feedDiscovery(this)
}

/** Typed feature handoffs accepted by the application navigation runtime. */
sealed interface AppNavigationEvent {
    data class Onboarding(val outcome: OnboardingOutcome) : AppNavigationEvent

    data class Home(val outcome: HomeOutcome) : AppNavigationEvent

    data class FeedDiscovery(val outcome: FeedDiscoveryOutcome.CandidateSelected) : AppNavigationEvent

    data object EditWebsite : AppNavigationEvent

    data object Back : AppNavigationEvent
}

/** Result that the app caller must consume outside navigation, if any. */
sealed interface AppNavigationResult {
    data object Handled : AppNavigationResult

    data object AtRoot : AppNavigationResult

    data class External(val outcome: FeedDiscoveryOutcome.CandidateSelected) : AppNavigationResult
}

/**
 * Authoritative Navigation 3 runtime shared by Android and iOS.
 *
 * The public [backStack] is read-only to callers; all mutations stay centralized in [handle].
 */
class AppNavigation internal constructor(internal val navigation3BackStack: NavBackStack<AppNavKey>) {
    constructor(
        initialBackStack: List<AppNavKey> = listOf(AppNavKey.Onboarding),
    ) : this(NavBackStack(*initialBackStack.toTypedArray()))

    init {
        require(navigation3BackStack.isNotEmpty()) {
            "App navigation requires a root destination."
        }
    }

    val backStack: List<AppNavKey>
        get() = navigation3BackStack

    /** Applies one feature handoff using the deterministic application stack rules. */
    fun handle(event: AppNavigationEvent): AppNavigationResult = when (event) {
        AppNavigationEvent.Back ->
            if (navigation3BackStack.size > 1) {
                navigation3BackStack.removeLast()
                AppNavigationResult.Handled
            } else {
                AppNavigationResult.AtRoot
            }

        is AppNavigationEvent.FeedDiscovery -> AppNavigationResult.External(event.outcome)

        is AppNavigationEvent.Home -> when (event.outcome) {
            HomeOutcome.FollowWebsite -> {
                navigation3BackStack += AppNavKey.Onboarding
                AppNavigationResult.Handled
            }
        }

        is AppNavigationEvent.Onboarding -> when (val outcome = event.outcome) {
            is OnboardingOutcome.FollowWebsite -> {
                navigation3BackStack += AppNavKey.FeedDiscovery(outcome.website)
                AppNavigationResult.Handled
            }

            OnboardingOutcome.UseApp -> {
                returnToHomeWithoutDuplicate()
                AppNavigationResult.Handled
            }
        }

        AppNavigationEvent.EditWebsite -> {
            if (navigation3BackStack.lastOrNull() is AppNavKey.FeedDiscovery) {
                navigation3BackStack.removeLast()
            }
            AppNavigationResult.Handled
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

/**
 * Remembers the typed Navigation 3 stack with the same explicit serialization configuration on every platform.
 */
@Composable
internal fun rememberAppNavigation(): AppNavigation {
    val backStack = rememberSerializable(
        serializer = serializer<NavBackStack<AppNavKey>>(),
        configuration = AppNavigationSavedStateConfiguration,
    ) {
        NavBackStack(AppNavKey.Onboarding)
    }
    return remember(backStack) { AppNavigation(backStack) }
}

private val AppNavigationSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(AppNavKey.Onboarding::class, AppNavKey.Onboarding.serializer())
            subclass(AppNavKey.Home::class, AppNavKey.Home.serializer())
            subclass(AppNavKey.FeedDiscovery::class, AppNavKey.FeedDiscovery.serializer())
        }
    }
}
