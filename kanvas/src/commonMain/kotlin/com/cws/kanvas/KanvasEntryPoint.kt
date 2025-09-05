package com.cws.kanvas

import androidx.compose.runtime.Composable

expect class KanvasEntryPoint {
    constructor(renderLoop: RenderLoop, content: @Composable (RenderLoop) -> Unit)
}