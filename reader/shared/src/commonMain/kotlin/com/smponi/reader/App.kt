package com.smponi.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smponi.reader.core.designsystem.LocalReaderDesignSystem
import com.smponi.reader.core.designsystem.ReaderTheme

@Composable
@Preview
fun App() {
    ReaderTheme {
        val designSystem = LocalReaderDesignSystem.current
        Box(
            modifier = Modifier
                .background(designSystem.colors.surface)
                .safeContentPadding()
                .fillMaxSize(),
        )
    }
}
