package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

actual val LocalScreenConfig: ProvidableCompositionLocal<ScreenConfig>
    @Composable get() {
        val config = LocalConfiguration.current
        return staticCompositionLocalOf {
            ScreenConfig(
                screenWidth = config.screenWidthDp.dp,
                screenHeight = config.screenHeightDp.dp
            )
        }
    }