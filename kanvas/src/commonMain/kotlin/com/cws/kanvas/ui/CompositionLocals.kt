package com.cws.kanvas.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.unit.Dp

data class ScreenConfig(
    val screenWidth: Dp,
    val screenHeight: Dp
)

expect val LocalScreenConfig: ProvidableCompositionLocal<ScreenConfig>