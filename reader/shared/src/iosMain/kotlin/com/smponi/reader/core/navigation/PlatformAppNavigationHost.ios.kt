@file:Suppress("DEPRECATION")

package com.smponi.reader.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.smponi.reader.core.designsystem.FoundationSpacing
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.resources.Res
import com.smponi.reader.resources.navigation_back
import org.jetbrains.compose.resources.stringResource

/**
 * Apple-appropriate adapter over the shared Navigation 3 stack.
 *
 * It uses an honest opaque navigation bar fallback and Compose's iOS back dispatcher. It owns no route graph.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformAppNavigationHost(navigation: AppNavigation, content: @Composable (AppNavKey) -> Unit) {
    val canGoBack = navigation.backStack.size > 1
    val currentKey = navigation.backStack.last()

    BackHandler(enabled = canGoBack) {
        navigation.handle(AppNavigationEvent.Back)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (canGoBack) {
            AppleNavigationBar(
                onBack = {
                    navigation.handle(AppNavigationEvent.Back)
                },
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            content(currentKey)
        }
    }
}

@Composable
private fun AppleNavigationBar(onBack: () -> Unit) {
    val design = LocalReaderDesignSystem.current
    val backLabel = stringResource(Res.string.navigation_back)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(design.colors.surfaceContainer)
            .padding(horizontal = FoundationSpacing.small),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .sizeIn(
                    minWidth = AppleNavigationTarget,
                    minHeight = AppleNavigationTarget,
                )
                .clickable(
                    role = Role.Button,
                    onClick = onBack,
                )
                .focusable()
                .semantics(mergeDescendants = true) {
                    role = Role.Button
                    contentDescription = backLabel
                }
                .padding(horizontal = FoundationSpacing.small),
        ) {
            BasicText(
                text = "‹ $backLabel",
                style = design.typography.body.copy(color = design.colors.accent),
            )
        }
    }
}

private val AppleNavigationTarget = 52.dp
