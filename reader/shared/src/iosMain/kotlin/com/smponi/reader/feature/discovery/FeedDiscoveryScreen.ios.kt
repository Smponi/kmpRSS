package com.smponi.reader.feature.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.core.navigation.navigationDestinationHeading

/** Apple-native-in-spirit discovery handoff using opaque Foundation surfaces rather than Material components. */
@Composable
internal actual fun PlatformFeedDiscoveryScreen(
    state: FeedDiscoveryState,
    onCandidateSelected: (FeedCandidate) -> Unit,
    onRetry: () -> Unit,
    onEditWebsite: () -> Unit,
) {
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
                .widthIn(max = DiscoveryContentMaxWidth)
                .verticalScroll(rememberScrollState())
                .padding(vertical = FoundationSpacing.extraLarge),
        ) {
            DiscoveryAppleText(
                text = state.appleTitle(),
                style = design.typography.display,
                color = design.colors.onSurface,
                modifier = Modifier.navigationDestinationHeading(),
            )
            Spacer(Modifier.height(FoundationSpacing.small))
            DiscoveryAppleText(
                text = state.website,
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(FoundationSpacing.extraLarge))
            DiscoveryAppleBody(
                state = state,
                onCandidateSelected = onCandidateSelected,
            )
            Spacer(Modifier.height(FoundationSpacing.extraLarge))
            if (state is FeedDiscoveryState.NoFeeds || state is FeedDiscoveryState.Failed) {
                DiscoveryAppleButton(
                    label = "Noch einmal suchen",
                    filled = true,
                    onClick = onRetry,
                )
                Spacer(Modifier.height(FoundationSpacing.small))
            }
            DiscoveryAppleButton(
                label = "Andere Website",
                filled = false,
                onClick = onEditWebsite,
            )
        }
    }
}

private fun FeedDiscoveryState.appleTitle(): String = when (this) {
    is FeedDiscoveryState.Discovering -> "Suche nach Feeds"
    is FeedDiscoveryState.Found ->
        if (candidates.size == 1) "Feed gefunden" else "${candidates.size} Feeds gefunden"

    is FeedDiscoveryState.NoFeeds -> "Keine Feeds gefunden"
    is FeedDiscoveryState.Failed -> "Website nicht erreichbar"
}

@Composable
private fun DiscoveryAppleBody(state: FeedDiscoveryState, onCandidateSelected: (FeedCandidate) -> Unit) {
    val design = LocalReaderDesignSystem.current
    when (state) {
        is FeedDiscoveryState.Discovering ->
            DiscoveryAppleStatusCard(
                description = "Die Website wird geladen und nach angebotenen Feeds durchsucht.",
                showStatusMark = true,
            )

        is FeedDiscoveryState.Found -> {
            DiscoveryAppleText(
                text = "Wähle einen Feed zum Öffnen:",
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
            Column(
                verticalArrangement = Arrangement.spacedBy(FoundationSpacing.small),
                modifier = Modifier.selectableGroup(),
            ) {
                state.candidates.forEach { candidate ->
                    DiscoveryAppleCandidate(
                        candidate = candidate,
                        selected = candidate == state.selectedCandidate,
                        onSelected = { onCandidateSelected(candidate) },
                    )
                }
            }
        }

        is FeedDiscoveryState.NoFeeds ->
            DiscoveryAppleStatusCard(
                description = "Unter dieser Adresse wurde kein unterstützter Feed angegeben. Versuche es erneut oder " +
                    "ändere die Website.",
            )

        is FeedDiscoveryState.Failed ->
            DiscoveryAppleStatusCard(
                description = "Die Website konnte gerade nicht geladen werden. Prüfe deine Verbindung oder versuche " +
                    "es erneut.",
            )
    }
}

@Composable
private fun DiscoveryAppleStatusCard(description: String, showStatusMark: Boolean = false) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .border(1.dp, design.colors.outline, shape)
            .padding(FoundationSpacing.large),
    ) {
        if (showStatusMark) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(3.dp, design.colors.accent, CircleShape)
                    .semantics {
                        contentDescription = "Feeds werden gesucht"
                    },
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
        }
        DiscoveryAppleText(
            text = description,
            style = design.typography.body,
            color = design.colors.onSurface,
        )
    }
}

@Composable
private fun DiscoveryAppleCandidate(candidate: FeedCandidate, selected: Boolean, onSelected: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(18.dp)
    val outline = if (selected) design.colors.accent else design.colors.outline
    val outlineWidth = if (selected) 2.dp else 1.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = CandidateMinimumHeight)
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .border(outlineWidth, outline, shape)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onSelected,
            )
            .focusable()
            .padding(FoundationSpacing.medium),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DiscoveryAppleText(
                text = candidate.title,
                style = design.typography.title,
                color = design.colors.onSurface,
            )
            Spacer(Modifier.height(FoundationSpacing.extraSmall))
            DiscoveryAppleText(
                text = candidate.url,
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
        }
        if (selected) {
            DiscoveryAppleText(
                text = "✓",
                style = design.typography.title,
                color = design.colors.accent,
                modifier = Modifier
                    .padding(start = FoundationSpacing.medium)
                    .clearAndSetSemantics {},
            )
        }
    }
}

@Composable
private fun DiscoveryAppleButton(label: String, filled: Boolean, onClick: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val foreground = if (filled) design.colors.onAccent else design.colors.accent
    val background = if (filled) design.colors.accent else Color.Transparent
    val shape = RoundedCornerShape(14.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = PrimaryActionHeight)
            .clip(shape)
            .background(background)
            .clickable(role = Role.Button, onClick = onClick)
            .focusable()
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = label
            }
            .padding(horizontal = FoundationSpacing.medium, vertical = FoundationSpacing.small),
    ) {
        DiscoveryAppleText(
            text = label,
            style = design.typography.label,
            color = foreground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DiscoveryAppleText(
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

private val DiscoveryContentMaxWidth = 560.dp
private val PrimaryActionHeight = 52.dp
private val CandidateMinimumHeight = 52.dp

@Preview
@Composable
private fun IOSFeedDiscoveryPreview() {
    ReaderTheme {
        PlatformFeedDiscoveryScreen(
            state = FeedDiscoveryState.Found(
                website = "https://example.com",
                candidates = listOf(
                    FeedCandidate(
                        title = "Artikel",
                        url = "https://example.com/articles.xml",
                    ),
                    FeedCandidate(
                        title = "Notizen",
                        url = "https://example.com/notes.xml",
                    ),
                ),
                selectedCandidate = FeedCandidate(
                    title = "Notizen",
                    url = "https://example.com/notes.xml",
                ),
            ),
            onCandidateSelected = {},
            onRetry = {},
            onEditWebsite = {},
        )
    }
}
