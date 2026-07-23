package com.smponi.reader.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSize
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.onboarding_android_body
import com.smponi.reader.resources.onboarding_android_field_label
import com.smponi.reader.resources.onboarding_android_field_supporting
import com.smponi.reader.resources.onboarding_android_follow_action
import com.smponi.reader.resources.onboarding_android_headline
import com.smponi.reader.resources.onboarding_android_panel_title
import com.smponi.reader.resources.onboarding_android_use_app_action
import com.smponi.reader.resources.onboarding_website_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * Material 3 Expressive entry screen for PRD-013.
 *
 * Compact windows keep one reading order; wide and landscape windows separate the promise from
 * the action surface without introducing another onboarding step.
 */
@Composable
internal actual fun PlatformOnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            val useWideLayout = maxWidth >= WideLayoutThreshold
            val horizontalPadding = if (useWideLayout) FoundationSpacing.large else FoundationSpacing.medium

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = FoundationSpacing.large),
            ) {
                if (useWideLayout) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(FoundationSpacing.extraLarge),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .widthIn(max = OnboardingWideContentMaxWidth)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .semantics { isTraversalGroup = true },
                    ) {
                        OnboardingIntroduction(
                            modifier = Modifier
                                .weight(1f)
                                .semantics { traversalIndex = 0f },
                        )
                        OnboardingActionPanel(
                            state = state,
                            onAction = onAction,
                            modifier = Modifier
                                .weight(1f)
                                .semantics { traversalIndex = 1f },
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.extraLarge),
                        modifier = Modifier
                            .widthIn(max = OnboardingCompactContentMaxWidth)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .semantics { isTraversalGroup = true },
                    ) {
                        OnboardingIntroduction(
                            modifier = Modifier.semantics { traversalIndex = 0f },
                        )
                        OnboardingActionPanel(
                            state = state,
                            onAction = onAction,
                            modifier = Modifier.semantics { traversalIndex = 1f },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingIntroduction(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_android_headline),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(Res.string.onboarding_android_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun OnboardingActionPanel(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
            modifier = Modifier.padding(FoundationSpacing.large),
        ) {
            Text(
                text = stringResource(Res.string.onboarding_android_panel_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            OutlinedTextField(
                value = state.website,
                onValueChange = { onAction(OnboardingAction.WebsiteChanged(it)) },
                label = { Text(stringResource(Res.string.onboarding_android_field_label)) },
                placeholder = { Text(stringResource(Res.string.onboarding_website_placeholder)) },
                supportingText = {
                    Text(stringResource(Res.string.onboarding_android_field_supporting))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (state.canFollowWebsite) {
                            onAction(OnboardingAction.FollowWebsite)
                        }
                    },
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { onAction(OnboardingAction.FollowWebsite) },
                enabled = state.canFollowWebsite,
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = PrimaryActionHeight),
            ) {
                Text(stringResource(Res.string.onboarding_android_follow_action))
            }
            TextButton(
                onClick = { onAction(OnboardingAction.UseApp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget),
            ) {
                Text(stringResource(Res.string.onboarding_android_use_app_action))
            }
        }
    }
}

private val WideLayoutThreshold = 720.dp
private val OnboardingCompactContentMaxWidth = 600.dp
private val OnboardingWideContentMaxWidth = 1040.dp
private val PrimaryActionHeight = 56.dp

@Preview
@Composable
private fun AndroidOnboardingPreview() {
    ReaderTheme {
        PlatformOnboardingScreen(
            state = OnboardingState(website = "example.com"),
            onAction = {},
        )
    }
}

@Preview(
    name = "Landscape and large text",
    widthDp = 900,
    heightDp = 500,
    fontScale = 1.6f,
)
@Composable
private fun AndroidOnboardingLandscapeLargeTextPreview() {
    ReaderTheme {
        PlatformOnboardingScreen(
            state = OnboardingState(),
            onAction = {},
        )
    }
}
