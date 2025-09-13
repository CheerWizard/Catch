package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun UiBox(
    modifier: UiModifier,
    content: @Composable (() -> Unit)?,
) {
    Div(modifier.toAttrs()) { content?.invoke() }
}