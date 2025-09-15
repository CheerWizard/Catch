package com.cws.kanvas.ui

import androidx.compose.ui.Alignment
import org.jetbrains.compose.web.css.alignContent

actual class UiBoxScope() {

    actual fun UiModifier.align(alignment: Alignment) = UiModifier {
        attrs?.invoke(this)
        style {
            alignContent(alignment.alignContent)
        }
    }

    actual fun UiModifier.matchParentSize() = fillMaxSize()

}