package com.cws.kanvas

import androidx.compose.runtime.Composable

actual class KanvasEntryPoint {

    actual constructor(renderLoop: RenderLoop, content: @Composable ((RenderLoop) -> Unit)) {
    }

}