package com.smponi.reader.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSize
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.home_empty_body
import com.smponi.reader.resources.home_empty_title
import com.smponi.reader.resources.home_follow_website
import com.smponi.reader.resources.home_inbox
import com.smponi.reader.resources.home_inbox_empty
import com.smponi.reader.resources.home_intro
import com.smponi.reader.resources.home_intro_supporting
import com.smponi.reader.resources.home_saved
import com.smponi.reader.resources.home_saved_empty
import com.smponi.reader.resources.home_sections_title
import com.smponi.reader.resources.home_title
import com.smponi.reader.resources.home_websites_and_tags
import com.smponi.reader.resources.home_websites_and_tags_empty
import org.jetbrains.compose.resources.stringResource

/** Material 3 Expressive Home shell with one-column and medium-window compositions. */
@Composable
internal actual fun PlatformHomeScreen(state: HomeState, onAction: (HomeAction) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val useWideLayout = maxWidth >= MediumWindowBreakpoint &&
                LocalDensity.current.fontScale < LARGE_TEXT_SINGLE_COLUMN_THRESHOLD
            when (state) {
                HomeState.Empty -> if (useWideLayout) {
                    WideEmptyHome(onFollowWebsite = { onAction(HomeAction.FollowWebsite) })
                } else {
                    CompactEmptyHome(onFollowWebsite = { onAction(HomeAction.FollowWebsite) })
                }
            }
        }
    }
}

@Composable
private fun CompactEmptyHome(onFollowWebsite: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.large),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = FoundationSpacing.medium,
                vertical = FoundationSpacing.large,
            ),
    ) {
        HomeHeader()
        EmptyReadingSpace(
            onFollowWebsite = onFollowWebsite,
            modifier = Modifier.fillMaxWidth(),
        )
        HomeSections(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun WideEmptyHome(onFollowWebsite: () -> Unit) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(FoundationSpacing.large),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .widthIn(max = HomeContentMaxWidth)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(FoundationSpacing.large),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FoundationSpacing.large),
                modifier = Modifier.width(HomeSidebarWidth),
            ) {
                HomeHeader()
                HomeSections(modifier = Modifier.fillMaxWidth())
            }
            EmptyReadingSpace(
                onFollowWebsite = onFollowWebsite,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(FoundationSpacing.small)) {
        Text(
            text = stringResource(Res.string.home_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = stringResource(Res.string.home_intro),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(Res.string.home_intro_supporting),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun EmptyReadingSpace(onFollowWebsite: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(FoundationSpacing.large),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(ReadingMarginWidth)
                    .clearAndSetSemantics { },
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(ReadingMarginWidth),
                ) {}
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = FoundationSpacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.home_empty_title),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    text = stringResource(Res.string.home_empty_body),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(FoundationSpacing.small))
                Button(
                    onClick = onFollowWebsite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = PrimaryActionHeight),
                ) {
                    Text(stringResource(Res.string.home_follow_website))
                }
            }
        }
    }
}

@Composable
private fun HomeSections(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(FoundationSpacing.medium)) {
            Text(
                text = stringResource(Res.string.home_sections_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = FoundationSpacing.small)
                    .semantics { heading() },
            )
            HomeStatusRow(
                label = stringResource(Res.string.home_inbox),
                status = stringResource(Res.string.home_inbox_empty),
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            HomeStatusRow(
                label = stringResource(Res.string.home_saved),
                status = stringResource(Res.string.home_saved_empty),
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            HomeStatusRow(
                label = stringResource(Res.string.home_websites_and_tags),
                status = stringResource(Res.string.home_websites_and_tags_empty),
            )
        }
    }
}

@Composable
private fun HomeStatusRow(label: String, status: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.extraSmall),
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
            .padding(vertical = FoundationSpacing.medium)
            .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget),
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(
            text = status,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private val MediumWindowBreakpoint = 600.dp
private const val LARGE_TEXT_SINGLE_COLUMN_THRESHOLD = 1.5f
private val HomeContentMaxWidth = 1040.dp
private val HomeSidebarWidth = 320.dp
private val ReadingMarginWidth = 4.dp
private val PrimaryActionHeight = 56.dp

@Preview(widthDp = 412, heightDp = 915)
@Composable
private fun AndroidCompactHomePreview() {
    ReaderTheme {
        PlatformHomeScreen(
            state = HomeState.Empty,
            onAction = {},
        )
    }
}

@Preview(widthDp = 900, heightDp = 700)
@Composable
private fun AndroidWideHomePreview() {
    ReaderTheme {
        PlatformHomeScreen(
            state = HomeState.Empty,
            onAction = {},
        )
    }
}
