package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
expect fun KText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    onClick: (() -> Unit)? = null
)