package com.cws.kanvas.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
actual fun UiText(
    modifier: UiModifier,
    text: String,
    style: TextStyle
) {
    Text(
        modifier = modifier.toModifier(),
        text = text,
        style = style
    )
}