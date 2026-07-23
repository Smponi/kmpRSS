package com.smponi.reader.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSize
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme
import com.smponi.reader.core.navigation.navigationDestinationHeading
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

/**
 * Apple-native-in-spirit Home shell.
 *
 * Compose owns no native glass control here, so the screen deliberately uses opaque Apple-like surfaces.
 */
@Composable
internal actual fun PlatformHomeScreen(state: HomeState, onAction: (HomeAction) -> Unit) {
    val design = LocalReaderDesignSystem.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(design.colors.surface),
    ) {
        val useWideLayout = maxWidth >= WideWindowBreakpoint &&
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

@Composable
private fun CompactEmptyHome(onFollowWebsite: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.large),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = FoundationSpacing.large,
                vertical = FoundationSpacing.extraLarge,
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
            horizontalArrangement = Arrangement.spacedBy(FoundationSpacing.extraLarge),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .widthIn(max = HomeContentMaxWidth)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(FoundationSpacing.extraLarge),
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
    val design = LocalReaderDesignSystem.current
    Column(verticalArrangement = Arrangement.spacedBy(FoundationSpacing.small)) {
        AppleText(
            text = stringResource(Res.string.home_title),
            style = design.typography.label,
            color = design.colors.accent,
        )
        AppleText(
            text = stringResource(Res.string.home_intro),
            style = design.typography.display,
            color = design.colors.onSurface,
            modifier = Modifier.navigationDestinationHeading(),
        )
        AppleText(
            text = stringResource(Res.string.home_intro_supporting),
            style = design.typography.body,
            color = design.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun EmptyReadingSpace(onFollowWebsite: () -> Unit, modifier: Modifier = Modifier) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(EmptyCardCornerRadius)

    Row(
        modifier = modifier
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .border(1.dp, design.colors.outline, shape)
            .height(IntrinsicSize.Min)
            .padding(FoundationSpacing.large),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(ReadingMarginWidth)
                .clip(RoundedCornerShape(ReadingMarginWidth))
                .background(design.colors.accent)
                .clearAndSetSemantics { },
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(FoundationSpacing.medium),
            modifier = Modifier
                .weight(1f)
                .padding(start = FoundationSpacing.large),
        ) {
            AppleText(
                text = stringResource(Res.string.home_empty_title),
                style = design.typography.title,
                color = design.colors.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            AppleText(
                text = stringResource(Res.string.home_empty_body),
                style = design.typography.body,
                color = design.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(FoundationSpacing.small))
            AppleButton(
                label = stringResource(Res.string.home_follow_website),
                onClick = onFollowWebsite,
            )
        }
    }
}

@Composable
private fun HomeSections(modifier: Modifier = Modifier) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(SectionCardCornerRadius)

    Column(
        modifier = modifier
            .clip(shape)
            .background(design.colors.surfaceContainer)
            .border(1.dp, design.colors.outline, shape)
            .padding(horizontal = FoundationSpacing.medium),
    ) {
        AppleText(
            text = stringResource(Res.string.home_sections_title),
            style = design.typography.title,
            color = design.colors.onSurface,
            modifier = Modifier
                .padding(top = FoundationSpacing.medium, bottom = FoundationSpacing.small)
                .semantics { heading() },
        )
        HomeStatusRow(
            label = stringResource(Res.string.home_inbox),
            status = stringResource(Res.string.home_inbox_empty),
        )
        AppleDivider()
        HomeStatusRow(
            label = stringResource(Res.string.home_saved),
            status = stringResource(Res.string.home_saved_empty),
        )
        AppleDivider()
        HomeStatusRow(
            label = stringResource(Res.string.home_websites_and_tags),
            status = stringResource(Res.string.home_websites_and_tags_empty),
        )
    }
}

@Composable
private fun HomeStatusRow(label: String, status: String) {
    val design = LocalReaderDesignSystem.current
    Column(
        verticalArrangement = Arrangement.spacedBy(FoundationSpacing.extraSmall),
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
            .padding(vertical = FoundationSpacing.medium)
            .sizeIn(minHeight = FoundationSize.minimumInteractiveTarget),
    ) {
        AppleText(
            text = label,
            style = design.typography.body,
            color = design.colors.onSurface,
        )
        AppleText(
            text = status,
            style = design.typography.label,
            color = design.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun AppleButton(label: String, onClick: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val shape = RoundedCornerShape(ButtonCornerRadius)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = PrimaryActionHeight)
            .clip(shape)
            .background(design.colors.accent)
            .clickable(role = Role.Button, onClick = onClick)
            .focusable()
            .semantics(mergeDescendants = true) { role = Role.Button }
            .padding(horizontal = FoundationSpacing.medium, vertical = FoundationSpacing.small),
    ) {
        AppleText(
            text = label,
            style = design.typography.label,
            color = design.colors.onAccent,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AppleDivider() {
    val design = LocalReaderDesignSystem.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(design.colors.outline),
    )
}

@Composable
private fun AppleText(
    text: String,
    style: TextStyle,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    BasicText(
        text = text,
        style = style.copy(color = color, textAlign = textAlign),
        modifier = modifier,
    )
}

private val WideWindowBreakpoint = 700.dp
private const val LARGE_TEXT_SINGLE_COLUMN_THRESHOLD = 1.5f
private val HomeContentMaxWidth = 1000.dp
private val HomeSidebarWidth = 300.dp
private val EmptyCardCornerRadius = 26.dp
private val SectionCardCornerRadius = 18.dp
private val ButtonCornerRadius = 14.dp
private val ReadingMarginWidth = 3.dp
private val PrimaryActionHeight = 52.dp

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun IOSCompactHomePreview() {
    ReaderTheme {
        PlatformHomeScreen(
            state = HomeState.Empty,
            onAction = {},
        )
    }
}

@Preview(widthDp = 920, heightDp = 700)
@Composable
private fun IOSWideHomePreview() {
    ReaderTheme {
        PlatformHomeScreen(
            state = HomeState.Empty,
            onAction = {},
        )
    }
}
