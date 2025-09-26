package com.cws.kanvas.ui

import androidx.compose.runtime.Composable

@Composable
expect fun UiBox(
    modifier: UiModifier = UiModifier(),
    content: @Composable (UiBoxScope.() -> Unit)? = null
)