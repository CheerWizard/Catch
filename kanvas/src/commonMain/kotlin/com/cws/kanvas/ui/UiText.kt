package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
expect fun UiText(
    modifier: UiModifier = UiModifier(),
    text: String,
    style: TextStyle
)