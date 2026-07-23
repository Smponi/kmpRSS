package com.smponi.reader.feature.discovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSize
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.MotionLevel
import com.smponi.reader.core.designsystem.ReaderTheme

/** Material 3 discovery handoff for Android; it shares state meaning, not layout chrome, with iOS. */
@Composable
internal actual fun PlatformFeedDiscoveryScreen(
    state: FeedDiscoveryState,
    onCandidateSelected: (FeedCandidate) -> Unit,
    onRetry: () -> Unit,
    onEditWebsite: () -> Unit,
) {
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
                    .widthIn(max = DiscoveryContentMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = FoundationSpacing.large),
            ) {
                DiscoveryHeading(state)
                Spacer(Modifier.height(FoundationSpacing.small))
                Text(
                    text = state.website,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(FoundationSpacing.extraLarge))
                DiscoveryBody(
                    state = state,
                    onCandidateSelected = onCandidateSelected,
                )
                Spacer(Modifier.height(FoundationSpacing.extraLarge))
                if (state is FeedDiscoveryState.NoFeeds || state is FeedDiscoveryState.Failed) {
                    Button(
                        onClick = onRetry,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = PrimaryActionHeight),
                    ) {
                        Text("Erneut suchen")
                    }
                    Spacer(Modifier.height(FoundationSpacing.small))
                }
                TextButton(
                    onClick = onEditWebsite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget),
                ) {
                    Text("Andere Website eingeben")
                }
            }
        }
    }
}

@Composable
private fun DiscoveryHeading(state: FeedDiscoveryState) {
    val title = when (state) {
        is FeedDiscoveryState.Discovering -> "Website wird geprüft"
        is FeedDiscoveryState.Found ->
            if (state.candidates.size == 1) "Feed gefunden" else "${state.candidates.size} Feeds gefunden"

        is FeedDiscoveryState.NoFeeds -> "Kein Feed gefunden"
        is FeedDiscoveryState.Failed -> "Website nicht erreichbar"
    }
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.semantics { heading() },
    )
}

@Composable
private fun DiscoveryBody(state: FeedDiscoveryState, onCandidateSelected: (FeedCandidate) -> Unit) {
    when (state) {
        is FeedDiscoveryState.Discovering -> {
            val reducedMotion =
                LocalReaderDesignSystem.current.accessibility.motionLevel == MotionLevel.Reduced
            if (!reducedMotion) {
                CircularProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = "Feeds werden gesucht"
                    },
                )
                Spacer(Modifier.height(FoundationSpacing.medium))
            }
            Text(
                text = "Wir suchen nach den Feeds, die diese Website anbietet.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        is FeedDiscoveryState.Found -> {
            Text(
                text = "Wähle den Feed, den du öffnen möchtest:",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(FoundationSpacing.medium))
            Column(
                verticalArrangement = Arrangement.spacedBy(FoundationSpacing.small),
                modifier = Modifier.selectableGroup(),
            ) {
                state.candidates.forEach { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        selected = candidate == state.selectedCandidate,
                        onSelected = { onCandidateSelected(candidate) },
                    )
                }
            }
        }

        is FeedDiscoveryState.NoFeeds ->
            Text(
                text = "Unter dieser Adresse wurde kein unterstützter Feed angegeben. Du kannst es erneut versuchen " +
                    "oder die Website ändern.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )

        is FeedDiscoveryState.Failed ->
            Text(
                text = "Die Website konnte gerade nicht geladen werden. Prüfe deine Verbindung oder " +
                    "versuche es erneut.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
    }
}

@Composable
private fun CandidateCard(candidate: FeedCandidate, selected: Boolean, onSelected: () -> Unit) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onSelected,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(FoundationSpacing.medium),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = candidate.title,
                    color = contentColor,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(FoundationSpacing.extraSmall))
                Text(
                    text = candidate.url,
                    color = contentColor,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            RadioButton(
                selected = selected,
                onClick = null,
                modifier = Modifier.clearAndSetSemantics {},
            )
        }
    }
}

private val DiscoveryContentMaxWidth = 640.dp
private val PrimaryActionHeight = 56.dp

@Preview
@Composable
private fun AndroidFeedDiscoveryPreview() {
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
