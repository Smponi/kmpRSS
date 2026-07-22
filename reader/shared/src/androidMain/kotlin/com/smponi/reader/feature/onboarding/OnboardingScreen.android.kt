package com.smponi.reader.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSize
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.ReaderTheme

/** Material 3 Expressive entry screen for PRD-013. There is intentionally no page progression. */
@Composable
internal actual fun PlatformOnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .imePadding()
                .padding(horizontal = FoundationSpacing.medium),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .widthIn(max = OnboardingContentMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = FoundationSpacing.large),
            ) {
                Text(
                    text = "Dein Netz. Deine Ruhe.",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(FoundationSpacing.medium))
                Text(
                    text = "Folge den Websites, die dir wichtig sind – privat und ohne algorithmischen Feed.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(FoundationSpacing.extraLarge))
                OutlinedTextField(
                    value = state.website,
                    onValueChange = { onAction(OnboardingAction.WebsiteChanged(it)) },
                    label = { Text("Website") },
                    placeholder = { Text("example.com") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Go,
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = { onAction(OnboardingAction.FollowWebsite) },
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(FoundationSpacing.medium))
                Button(
                    onClick = { onAction(OnboardingAction.FollowWebsite) },
                    enabled = state.website.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = PrimaryActionHeight),
                ) {
                    Text("Website folgen")
                }
                Spacer(Modifier.height(FoundationSpacing.small))
                TextButton(
                    onClick = { onAction(OnboardingAction.UseApp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget),
                ) {
                    Text("Erst einmal zur App")
                }
            }
        }
    }
}

private val OnboardingContentMaxWidth = 560.dp
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
