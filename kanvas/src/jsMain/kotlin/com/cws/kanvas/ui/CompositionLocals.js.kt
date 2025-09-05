package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import kotlinx.browser.window

actual val LocalScreenConfig: ProvidableCompositionLocal<ScreenConfig>
    @Composable get() {
        return staticCompositionLocalOf {
            ScreenConfig(
                screenWidth = window.innerWidth.dp,
                screenHeight = window.innerHeight.dp
            )
        }
    }