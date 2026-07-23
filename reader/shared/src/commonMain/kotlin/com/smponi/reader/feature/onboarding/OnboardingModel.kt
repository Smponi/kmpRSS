package com.smponi.reader.feature.onboarding

/** The editable information and derived action availability shown by the one-screen onboarding experience. */
data class OnboardingState(val website: String = "") {
    val canFollowWebsite: Boolean
        get() = website.isNotBlank()
}

/** User intent accepted by [OnboardingModel]. */
sealed interface OnboardingAction {
    data class WebsiteChanged(val value: String) : OnboardingAction

    data object FollowWebsite : OnboardingAction

    data object UseApp : OnboardingAction
}

/** A meaningful next product action; onboarding does not perform feed discovery itself. */
sealed interface OnboardingOutcome {
    data class FollowWebsite(val website: String) : OnboardingOutcome

    data object UseApp : OnboardingOutcome
}

/**
 * Small state seam shared by the platform-specific onboarding screens.
 *
 * It owns only onboarding behaviour. Feed discovery and app navigation consume its outcomes.
 */
class OnboardingModel(initialState: OnboardingState = OnboardingState()) {
    var state: OnboardingState = initialState
        private set

    fun onAction(action: OnboardingAction): OnboardingOutcome? = when (action) {
        is OnboardingAction.WebsiteChanged -> {
            state = state.copy(website = action.value)
            null
        }

        OnboardingAction.FollowWebsite ->
            state.website
                .trim()
                .takeIf(String::isNotEmpty)
                ?.let(OnboardingOutcome::FollowWebsite)

        OnboardingAction.UseApp -> OnboardingOutcome.UseApp
    }
}
