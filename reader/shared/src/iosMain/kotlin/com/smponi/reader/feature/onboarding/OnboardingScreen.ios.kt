package com.smponi.reader.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem

/** Apple-native-in-spirit entry screen using Foundation controls rather than Material components. */
@Composable
internal actual fun PlatformOnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    val design = LocalReaderDesignSystem.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(design.colors.surface)
            .imePadding()
            .padding(horizontal = FoundationSpacing.large),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .widthIn(max = OnboardingContentMaxWidth)
                .verticalScroll(rememberScrollState())
                .padding(vertical = FoundationSpacing.extraLarge),
        ) {
            AppleText(
                text = "Lies, was du gewählt hast.",
                style = design.typography.display,
                color = design.colors.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
            AppleText(
                text = "Deine Websites, ruhig gesammelt. Privat auf diesem Gerät.",
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(FoundationSpacing.extraLarge))
            WebsiteField(
                value = state.website,
                onValueChange = { onAction(OnboardingAction.WebsiteChanged(it)) },
                onSubmit = { onAction(OnboardingAction.FollowWebsite) },
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
            AppleButton(
                label = "Website folgen",
                enabled = state.website.isNotBlank(),
                filled = true,
                onClick = { onAction(OnboardingAction.FollowWebsite) },
            )
            Spacer(Modifier.height(FoundationSpacing.small))
            AppleButton(
                label = "Direkt zur App",
                enabled = true,
                filled = false,
                onClick = { onAction(OnboardingAction.UseApp) },
            )
        }
    }
}

@Composable
private fun WebsiteField(value: String, onValueChange: (String) -> Unit, onSubmit: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(12.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = design.typography.body.copy(color = design.colors.onSurface),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { onSubmit() }),
        decorationBox = { input ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, design.colors.outline, shape)
                    .padding(horizontal = FoundationSpacing.medium),
            ) {
                if (value.isEmpty()) {
                    AppleText(
                        text = "example.com",
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
            .semantics { contentDescription = "Website" },
    )
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
        !enabled && filled -> design.colors.outline.copy(alpha = 0.24f)
        filled -> design.colors.accent
        else -> Color.Transparent
    }
    val shape = RoundedCornerShape(14.dp)

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

private val OnboardingContentMaxWidth = 520.dp
private val WebsiteFieldHeight = 52.dp
private val PrimaryActionHeight = 52.dp
