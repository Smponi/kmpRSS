package com.smponi.reader.core.navigation

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics

/**
 * Marks and focuses the first visible heading when a destination enters composition.
 *
 * This gives screen-reader, keyboard and switch users a deterministic starting point after navigation.
 */
@Composable
internal fun Modifier.navigationDestinationHeading(): Modifier {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
    return focusRequester(focusRequester)
        .focusable()
        .semantics { heading() }
}
