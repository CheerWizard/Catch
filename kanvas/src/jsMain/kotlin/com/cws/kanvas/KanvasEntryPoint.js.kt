package com.cws.kanvas

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

class KanvasEntryPoint {

    constructor(renderLoop: RenderLoop, content: @Composable ((RenderLoop) -> Unit)) {
        renderComposable(KANVAS_ROOT) {
            // RenderLoop
            Canvas(attrs = {
                id(KANVAS_RUNTIME)
                style {
                    position(Position.Absolute)
                    top(0.px)
                    left(0.px)
                    width(100.percent)
                    height(100.percent)
                    property("z-index", "0")
                }
                ref {
                    renderLoop.startLoop()
                    onDispose { renderLoop.stopLoop() }
                }
            })
            // Compose UI
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    top(0.px)
                    left(0.px)
                    width(100.percent)
                    height(100.percent)
                    property("z-index", "1")
                }
            }) {
                content(renderLoop)
            }
        }
    }

}