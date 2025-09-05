package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import java.awt.Toolkit

actual val LocalScreenConfig: ProvidableCompositionLocal<ScreenConfig>
    @Composable get() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        return staticCompositionLocalOf {
            ScreenConfig(
                screenWidth = screenSize.width.dp,
                screenHeight = screenSize.height.dp
            )
        }
    }