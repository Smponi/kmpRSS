package com.smponi.reader.feature.preview

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.core.navigation.navigationDestinationHeading
import com.smponi.reader.feature.discovery.FeedCandidate
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.feed_preview_edit_selection
import com.smponi.reader.resources.feed_preview_feed_address_label
import com.smponi.reader.resources.feed_preview_ios_intro
import com.smponi.reader.resources.feed_preview_ios_title
import com.smponi.reader.resources.feed_preview_no_articles_body
import com.smponi.reader.resources.feed_preview_no_articles_title
import com.smponi.reader.resources.feed_preview_website_label
import org.jetbrains.compose.resources.stringResource

/** Apple-native-in-spirit preview using grouped opaque surfaces as the honest glass fallback. */
@Composable
internal actual fun PlatformFeedPreviewScreen(state: FeedPreviewState, onEditSelection: () -> Unit) {
    val design = LocalReaderDesignSystem.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(design.colors.surface)
            .padding(horizontal = FoundationSpacing.large),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .widthIn(max = PreviewContentMaxWidth)
                .verticalScroll(rememberScrollState())
                .padding(vertical = FoundationSpacing.extraLarge),
        ) {
            PreviewAppleText(
                text = stringResource(Res.string.feed_preview_ios_title),
                style = design.typography.display,
                color = design.colors.onSurface,
                modifier = Modifier.navigationDestinationHeading(),
            )
            Spacer(Modifier.height(FoundationSpacing.small))
            PreviewAppleText(
                text = stringResource(Res.string.feed_preview_ios_intro),
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(FoundationSpacing.extraLarge))
            AppleMetadataGroup(state)
            Spacer(Modifier.height(FoundationSpacing.medium))
            AppleNoArticlesGroup()
            Spacer(Modifier.height(FoundationSpacing.extraLarge))
            AppleEditButton(onEditSelection)
        }
    }
}

@Composable
private fun AppleMetadataGroup(state: FeedPreviewState) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .border(1.dp, design.colors.outline, shape)
            .semantics(mergeDescendants = true) {}
            .padding(FoundationSpacing.large),
    ) {
        PreviewAppleText(
            text = state.candidate.title,
            style = design.typography.title,
            color = design.colors.onSurface,
        )
        Spacer(Modifier.height(FoundationSpacing.large))
        AppleMetadata(
            label = stringResource(Res.string.feed_preview_website_label),
            value = state.website,
        )
        Spacer(Modifier.height(FoundationSpacing.medium))
        AppleMetadata(
            label = stringResource(Res.string.feed_preview_feed_address_label),
            value = state.candidate.url,
        )
    }
}

@Composable
private fun AppleMetadata(label: String, value: String) {
    val design = LocalReaderDesignSystem.current
    Column {
        PreviewAppleText(
            text = label,
            style = design.typography.label,
            color = design.colors.onSurfaceVariant,
        )
        Spacer(Modifier.height(FoundationSpacing.extraSmall))
        PreviewAppleText(
            text = value,
            style = design.typography.body,
            color = design.colors.onSurface,
        )
    }
}

@Composable
private fun AppleNoArticlesGroup() {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .padding(FoundationSpacing.large),
    ) {
        PreviewAppleText(
            text = stringResource(Res.string.feed_preview_no_articles_title),
            style = design.typography.title,
            color = design.colors.onSurface,
        )
        Spacer(Modifier.height(FoundationSpacing.small))
        PreviewAppleText(
            text = stringResource(Res.string.feed_preview_no_articles_body),
            style = design.typography.body,
            color = design.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun AppleEditButton(onClick: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val label = stringResource(Res.string.feed_preview_edit_selection)
    val shape = RoundedCornerShape(14.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = PreviewActionHeight)
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick)
            .focusable()
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = label
            }
            .padding(horizontal = FoundationSpacing.medium, vertical = FoundationSpacing.small),
    ) {
        PreviewAppleText(
            text = label,
            style = design.typography.label,
            color = design.colors.accent,
        )
    }
}

@Composable
private fun PreviewAppleText(text: String, style: TextStyle, color: Color, modifier: Modifier = Modifier) {
    BasicText(
        text = text,
        style = style.copy(color = color),
        modifier = modifier,
    )
}

private val PreviewContentMaxWidth = 560.dp
private val PreviewActionHeight = 52.dp

@Preview
@Composable
private fun IOSFeedPreviewPreview() {
    ReaderTheme {
        PlatformFeedPreviewScreen(
            state = FeedPreviewState(
                website = "https://example.com",
                candidate = FeedCandidate(
                    title = "Example updates",
                    url = "https://example.com/feed.xml",
                ),
            ),
            onEditSelection = {},
        )
    }
}
