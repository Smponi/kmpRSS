package com.smponi.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.feature.discovery.FeedDiscoveryFeature
import com.smponi.reader.feature.onboarding.OnboardingFeature
import com.smponi.reader.feature.onboarding.OnboardingOutcome

@Composable
@Preview
fun App(onOnboardingOutcome: (OnboardingOutcome) -> Unit = {}) {
    ReaderTheme {
        val designSystem = LocalReaderDesignSystem.current
        var discoveryOutcome by remember { mutableStateOf<OnboardingOutcome.FollowWebsite?>(null) }
        Box(
            modifier = Modifier
                .background(designSystem.colors.surface)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            val outcome = discoveryOutcome
            if (outcome == null) {
                OnboardingFeature { onboardingOutcome ->
                    onOnboardingOutcome(onboardingOutcome)
                    if (onboardingOutcome is OnboardingOutcome.FollowWebsite) {
                        discoveryOutcome = onboardingOutcome
                    }
                }
            } else {
                FeedDiscoveryFeature(
                    outcome = outcome,
                    onEditWebsite = { discoveryOutcome = null },
                )
            }
        }
    }
}
