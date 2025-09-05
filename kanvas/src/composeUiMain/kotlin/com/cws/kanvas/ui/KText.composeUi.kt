package com.cws.kanvas.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
actual fun KText(
    modifier: Modifier,
    text: String,
    style: TextStyle,
    onClick: (() -> Unit)?
) {
    Text(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            ),
        text = text,
        style = style,
    )
}