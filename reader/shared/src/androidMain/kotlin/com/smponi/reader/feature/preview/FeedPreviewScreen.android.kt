package com.smponi.reader.feature.preview

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.core.navigation.navigationDestinationHeading
import com.smponi.reader.feature.discovery.FeedCandidate
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.feed_preview_android_intro
import com.smponi.reader.resources.feed_preview_android_title
import com.smponi.reader.resources.feed_preview_edit_selection
import com.smponi.reader.resources.feed_preview_feed_address_label
import com.smponi.reader.resources.feed_preview_no_articles_body
import com.smponi.reader.resources.feed_preview_no_articles_title
import com.smponi.reader.resources.feed_preview_website_label
import org.jetbrains.compose.resources.stringResource

/** Material 3 Expressive read-only preview using tonal hierarchy and no speculative article rows. */
@Composable
internal actual fun PlatformFeedPreviewScreen(state: FeedPreviewState, onEditSelection: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = FoundationSpacing.medium),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .widthIn(max = PreviewContentMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = FoundationSpacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.feed_preview_android_title),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.navigationDestinationHeading(),
                )
                Spacer(Modifier.height(FoundationSpacing.small))
                Text(
                    text = stringResource(Res.string.feed_preview_android_intro),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(FoundationSpacing.extraLarge))
                CandidateMetadataCard(state)
                Spacer(Modifier.height(FoundationSpacing.medium))
                NoArticlesCard()
                Spacer(Modifier.height(FoundationSpacing.extraLarge))
                FilledTonalButton(
                    onClick = onEditSelection,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = PreviewActionHeight),
                ) {
                    Text(stringResource(Res.string.feed_preview_edit_selection))
                }
            }
        }
    }
}

@Composable
private fun CandidateMetadataCard(state: FeedPreviewState) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
    ) {
        Column(modifier = Modifier.padding(FoundationSpacing.large)) {
            Text(
                text = state.candidate.title,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(FoundationSpacing.large))
            Metadata(
                label = stringResource(Res.string.feed_preview_website_label),
                value = state.website,
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
            Metadata(
                label = stringResource(Res.string.feed_preview_feed_address_label),
                value = state.candidate.url,
            )
        }
    }
}

@Composable
private fun Metadata(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(Modifier.height(FoundationSpacing.extraSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun NoArticlesCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(FoundationSpacing.large)) {
            Text(
                text = stringResource(Res.string.feed_preview_no_articles_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(FoundationSpacing.small))
            Text(
                text = stringResource(Res.string.feed_preview_no_articles_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

private val PreviewContentMaxWidth = 640.dp
private val PreviewActionHeight = 56.dp

@Preview
@Composable
private fun AndroidFeedPreviewPreview() {
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
