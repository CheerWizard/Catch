package com.cws.kanvas.ui

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.VerticalAlignmentLine
import org.jetbrains.compose.web.css.flex

actual class UiColumnScope() {

    actual fun UiModifier.weight(
        weight: Float,
        fill: Boolean,
    ) = UiModifier {
        attrs?.invoke(this)
        style {
            flex((weight * 10).toInt())
        }
    }

    actual fun UiModifier.align(alignment: Alignment.Horizontal): UiModifier {
        // TODO not implemented
        return UiModifier(attrs)
    }

    actual fun UiModifier.alignBy(alignmentLine: VerticalAlignmentLine): UiModifier {
        // TODO not implemented
        return UiModifier(attrs)
    }

    actual fun UiModifier.alignBy(alignmentLineBlock: (Measured) -> Int): UiModifier {
        // TODO not implemented
        return UiModifier(attrs)
    }

}