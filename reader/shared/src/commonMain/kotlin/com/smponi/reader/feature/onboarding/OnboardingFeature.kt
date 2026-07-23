package com.smponi.reader.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Connects the shared onboarding behaviour to the platform-owned screen.
 *
 * The caller owns navigation to feed discovery or the app and therefore receives only meaningful outcomes.
 */
@Composable
fun OnboardingFeature(onOutcome: (OnboardingOutcome) -> Unit) {
    val model = remember { OnboardingModel() }
    var state by remember { mutableStateOf(model.state) }

    PlatformOnboardingScreen(
        state = state,
        onAction = { action ->
            val outcome = model.onAction(action)
            state = model.state
            if (outcome != null) onOutcome(outcome)
        },
    )
}

/** Platform rendering seam: Android and iOS share behaviour, not component chrome. */
@Composable
internal expect fun PlatformOnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit)
