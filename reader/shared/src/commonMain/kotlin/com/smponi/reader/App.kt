package com.smponi.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.core.navigation.AppNavKey
import com.smponi.reader.core.navigation.AppNavigation
import com.smponi.reader.core.navigation.AppNavigationEvent
import com.smponi.reader.core.navigation.AppNavigationResult
import com.smponi.reader.core.navigation.PlatformAppNavigationHost
import com.smponi.reader.core.navigation.provideAppEntry
import com.smponi.reader.core.navigation.rememberAppNavigation
import com.smponi.reader.feature.discovery.FeedDiscoveryFeature
import com.smponi.reader.feature.discovery.FeedDiscoveryOutcome
import com.smponi.reader.feature.home.HomeFeature
import com.smponi.reader.feature.home.openHome
import com.smponi.reader.feature.onboarding.OnboardingFeature
import com.smponi.reader.feature.onboarding.OnboardingOutcome

@Composable
@Preview
fun App(
    onOnboardingOutcome: (OnboardingOutcome) -> Unit = {},
    onFeedDiscoveryOutcome: (FeedDiscoveryOutcome) -> Unit = {},
) {
    ReaderTheme {
        val designSystem = LocalReaderDesignSystem.current
        val navigation = rememberAppNavigation()
        Box(
            modifier = Modifier
                .background(designSystem.colors.surface)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            PlatformAppNavigationHost(navigation) { key ->
                AppEntry(
                    key = key,
                    navigation = navigation,
                    onOnboardingOutcome = onOnboardingOutcome,
                    onFeedDiscoveryOutcome = onFeedDiscoveryOutcome,
                )
            }
        }
    }
}

@Composable
private fun AppEntry(
    key: AppNavKey,
    navigation: AppNavigation,
    onOnboardingOutcome: (OnboardingOutcome) -> Unit,
    onFeedDiscoveryOutcome: (FeedDiscoveryOutcome) -> Unit,
) {
    key.provideAppEntry(
        onboarding = {
            OnboardingFeature { outcome ->
                onOnboardingOutcome(outcome)
                navigation.handle(AppNavigationEvent.Onboarding(outcome))
            }
        },
        home = {
            val home = remember { openHome() }
            HomeFeature(home) { outcome ->
                navigation.handle(AppNavigationEvent.Home(outcome))
            }
        },
        feedDiscovery = { discoveryKey ->
            FeedDiscoveryFeature(
                outcome = OnboardingOutcome.FollowWebsite(discoveryKey.website),
                onCandidateSelected = { outcome ->
                    val result = navigation.handle(AppNavigationEvent.FeedDiscovery(outcome))
                    check(result is AppNavigationResult.External)
                    onFeedDiscoveryOutcome(result.outcome)
                },
                onEditWebsite = {
                    navigation.handle(AppNavigationEvent.EditWebsite)
                },
            )
        },
    )
}
