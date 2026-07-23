package com.smponi.reader.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.onboarding_ios_body
import com.smponi.reader.resources.onboarding_ios_field_label
import com.smponi.reader.resources.onboarding_ios_field_supporting
import com.smponi.reader.resources.onboarding_ios_follow_action
import com.smponi.reader.resources.onboarding_ios_headline
import com.smponi.reader.resources.onboarding_ios_panel_title
import com.smponi.reader.resources.onboarding_ios_use_app_action
import com.smponi.reader.resources.onboarding_website_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * Apple-native-in-spirit entry screen built from Compose Foundation.
 *
 * The action panel is deliberately opaque. Real Liquid Glass belongs to a supported native
 * host control; this renderer never imitates it with a synthetic blur.
 */
@Composable
internal actual fun PlatformOnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    val design = LocalReaderDesignSystem.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(design.colors.surface)
            .imePadding(),
    ) {
        val useWideLayout = maxWidth >= WideLayoutThreshold
        val horizontalPadding = if (useWideLayout) FoundationSpacing.extraLarge else FoundationSpacing.large

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

@Composable
private fun OnboardingIntroduction(modifier: Modifier = Modifier) {
    val design = LocalReaderDesignSystem.current

    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
        modifier = modifier,
    ) {
        AppleText(
            text = stringResource(Res.string.onboarding_ios_headline),
            style = design.typography.display,
            color = design.colors.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        AppleText(
            text = stringResource(Res.string.onboarding_ios_body),
            style = design.typography.body,
            color = design.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun OnboardingActionPanel(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val design = LocalReaderDesignSystem.current
    val panelShape = RoundedCornerShape(PanelCornerRadius)

    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
        modifier = modifier
            .fillMaxWidth()
            .clip(panelShape)
            .background(design.colors.surfaceContainer)
            .border(1.dp, design.colors.outline, panelShape)
            .padding(FoundationSpacing.large),
    ) {
        AppleText(
            text = stringResource(Res.string.onboarding_ios_panel_title),
            style = design.typography.title,
            color = design.colors.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        WebsiteField(
            value = state.website,
            onValueChange = { onAction(OnboardingAction.WebsiteChanged(it)) },
            onSubmit = {
                if (state.canFollowWebsite) {
                    onAction(OnboardingAction.FollowWebsite)
                }
            },
        )
        AppleButton(
            label = stringResource(Res.string.onboarding_ios_follow_action),
            enabled = state.canFollowWebsite,
            filled = true,
            onClick = { onAction(OnboardingAction.FollowWebsite) },
        )
        AppleButton(
            label = stringResource(Res.string.onboarding_ios_use_app_action),
            enabled = true,
            filled = false,
            onClick = { onAction(OnboardingAction.UseApp) },
        )
    }
}

@Composable
private fun WebsiteField(value: String, onValueChange: (String) -> Unit, onSubmit: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val fieldShape = RoundedCornerShape(FieldCornerRadius)
    val label = stringResource(Res.string.onboarding_ios_field_label)
    val borderColor = if (isFocused) design.colors.accent else design.colors.outline
    val borderWidth = if (isFocused) 2.dp else 1.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.small),
        modifier = Modifier.fillMaxWidth(),
    ) {
        AppleText(
            text = label,
            style = design.typography.label,
            color = design.colors.onSurface,
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = design.typography.body.copy(color = design.colors.onSurface),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { onSubmit() }),
            interactionSource = interactionSource,
            decorationBox = { input ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(fieldShape)
                        .background(design.colors.surface)
                        .border(borderWidth, borderColor, fieldShape)
                        .padding(horizontal = FoundationSpacing.medium),
                ) {
                    if (value.isEmpty()) {
                        AppleText(
                            text = stringResource(Res.string.onboarding_website_placeholder),
                            style = design.typography.body,
                            color = design.colors.onSurfaceVariant,
                        )
                    }
                    input()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = WebsiteFieldHeight)
                .semantics { contentDescription = label },
        )
        AppleText(
            text = stringResource(Res.string.onboarding_ios_field_supporting),
            style = design.typography.body,
            color = design.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun AppleButton(label: String, enabled: Boolean, filled: Boolean, onClick: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val foreground = when {
        !enabled -> design.colors.onSurfaceVariant
        filled -> design.colors.onAccent
        else -> design.colors.accent
    }
    val background = when {
        !enabled && filled -> design.colors.surface
        filled -> design.colors.accent
        else -> Color.Transparent
    }
    val shape = RoundedCornerShape(ButtonCornerRadius)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = PrimaryActionHeight)
            .clip(shape)
            .background(background)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .focusable(enabled)
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = label
                if (!enabled) disabled()
            }
            .padding(horizontal = FoundationSpacing.medium, vertical = FoundationSpacing.small),
    ) {
        AppleText(
            text = label,
            style = design.typography.label,
            color = foreground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AppleText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    androidx.compose.foundation.text.BasicText(
        text = text,
        style = style.copy(color = color, textAlign = textAlign),
        modifier = modifier,
    )
}

private val WideLayoutThreshold = 720.dp
private val OnboardingCompactContentMaxWidth = 560.dp
private val OnboardingWideContentMaxWidth = 1000.dp
private val PanelCornerRadius = 28.dp
private val FieldCornerRadius = 14.dp
private val ButtonCornerRadius = 14.dp
private val WebsiteFieldHeight = 52.dp
private val PrimaryActionHeight = 52.dp

@Preview
@Composable
private fun IOSOnboardingPreview() {
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
private fun IOSOnboardingLandscapeLargeTextPreview() {
    ReaderTheme {
        PlatformOnboardingScreen(
            state = OnboardingState(),
            onAction = {},
        )
    }
}
