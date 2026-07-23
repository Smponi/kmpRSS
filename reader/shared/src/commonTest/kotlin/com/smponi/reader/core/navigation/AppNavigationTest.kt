package com.smponi.reader.core.navigation

import com.smponi.reader.feature.home.HomeOutcome
import com.smponi.reader.feature.onboarding.OnboardingOutcome
import kotlin.test.Test
import kotlin.test.assertEquals

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
}
