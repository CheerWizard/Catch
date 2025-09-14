package com.cws.kanvas.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

@Composable
actual fun UiBox(
    modifier: UiModifier,
    content: @Composable (UiBoxScope.() -> Unit)?,
) {
    Box(modifier.modifier) { content?.invoke(UiBoxScope(this)) }
}