package com.smponi.reader.core.navigation

import com.smponi.reader.feature.discovery.FeedCandidate
import com.smponi.reader.feature.discovery.FeedDiscoveryOutcome
import com.smponi.reader.feature.home.HomeOutcome
import com.smponi.reader.feature.onboarding.OnboardingOutcome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class AppNavigationTest {
    @Test
    fun `onboarding and home share one website journey without duplicating home`() {
        val navigation = AppNavigation()

        assertEquals(listOf(AppNavKey.Onboarding), navigation.backStack)

        navigation.handle(AppNavigationEvent.Onboarding(OnboardingOutcome.UseApp))

        assertEquals(listOf(AppNavKey.Home), navigation.backStack)

        navigation.handle(AppNavigationEvent.Home(HomeOutcome.FollowWebsite))

        assertEquals(
            listOf(AppNavKey.Home, AppNavKey.Onboarding),
            navigation.backStack,
        )

        navigation.handle(
            AppNavigationEvent.Onboarding(
                OnboardingOutcome.FollowWebsite("example.com"),
            ),
        )

        assertEquals(
            listOf(
                AppNavKey.Home,
                AppNavKey.Onboarding,
                AppNavKey.FeedDiscovery("example.com"),
            ),
            navigation.backStack,
        )

        navigation.handle(AppNavigationEvent.EditWebsite)
        navigation.handle(AppNavigationEvent.Onboarding(OnboardingOutcome.UseApp))

        assertEquals(listOf(AppNavKey.Home), navigation.backStack)
    }

    @Test
    fun `candidate selection stays an external handoff`() {
        val navigation = AppNavigation(
            initialBackStack = listOf(
                AppNavKey.Onboarding,
                AppNavKey.FeedDiscovery("example.com"),
            ),
        )
        val outcome = FeedDiscoveryOutcome.CandidateSelected(
            website = "https://example.com",
            candidate = FeedCandidate(
                title = "Example feed",
                url = "https://example.com/feed.xml",
            ),
        )

        val result = navigation.handle(AppNavigationEvent.FeedDiscovery(outcome))

        assertEquals(AppNavigationResult.External(outcome), result)
        assertEquals(
            listOf(
                AppNavKey.Onboarding,
                AppNavKey.FeedDiscovery("example.com"),
            ),
            navigation.backStack,
        )
    }

    @Test
    fun `back pops only entries above the current root`() {
        val navigation = AppNavigation(
            initialBackStack = listOf(
                AppNavKey.Home,
                AppNavKey.Onboarding,
                AppNavKey.FeedDiscovery("example.com"),
            ),
        )

        assertEquals(
            AppNavigationResult.Handled,
            navigation.handle(AppNavigationEvent.Back),
        )
        assertEquals(
            listOf(AppNavKey.Home, AppNavKey.Onboarding),
            navigation.backStack,
        )

        assertEquals(
            AppNavigationResult.Handled,
            navigation.handle(AppNavigationEvent.Back),
        )
        assertEquals(listOf(AppNavKey.Home), navigation.backStack)

        assertEquals(
            AppNavigationResult.AtRoot,
            navigation.handle(AppNavigationEvent.Back),
        )
        assertEquals(listOf(AppNavKey.Home), navigation.backStack)
    }

    @Test
    fun `every unique key and the typed stack restore through serialization`() {
        val keys = listOf(
            AppNavKey.Onboarding,
            AppNavKey.Home,
            AppNavKey.FeedDiscovery("example.com"),
            AppNavKey.FeedDiscovery("news.example.com"),
        )

        val encoded = Json.encodeToString(keys)
        val restoredKeys = Json.decodeFromString<List<AppNavKey>>(encoded)
        val restoredNavigation = AppNavigation(restoredKeys)

        assertEquals(keys.size, keys.toSet().size)
        assertEquals(
            AppNavKey.FeedDiscovery("example.com"),
            AppNavKey.FeedDiscovery("example.com"),
        )
        assertEquals(keys, restoredKeys)
        assertEquals(keys, restoredNavigation.backStack)
    }

    @Test
    fun `entry provider resolves every concrete key exhaustively`() {
        val keys = listOf(
            AppNavKey.Onboarding,
            AppNavKey.Home,
            AppNavKey.FeedDiscovery("example.com"),
        )

        val entries = keys.map { key ->
            key.provideAppEntry(
                onboarding = { "onboarding" },
                home = { "home" },
                feedDiscovery = { "discovery:${it.website}" },
            )
        }

        assertEquals(
            listOf("onboarding", "home", "discovery:example.com"),
            entries,
        )
    }
}
