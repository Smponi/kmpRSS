package com.smponi.reader.feature.onboarding

import kotlin.test.Test
import kotlin.test.assertEquals

class OnboardingModelTest {
    @Test
    fun `website input leads directly to following that website`() {
        val onboarding = OnboardingModel()

        onboarding.onAction(OnboardingAction.WebsiteChanged("  example.com  "))

        assertEquals(
            OnboardingOutcome.FollowWebsite("example.com"),
            onboarding.onAction(OnboardingAction.FollowWebsite),
        )
    }

    @Test
    fun `newcomer can use the app without completing setup`() {
        val onboarding = OnboardingModel()

        assertEquals(
            OnboardingOutcome.UseApp,
            onboarding.onAction(OnboardingAction.UseApp),
        )
    }
}
