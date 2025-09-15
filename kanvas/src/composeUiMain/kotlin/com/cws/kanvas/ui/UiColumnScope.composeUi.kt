package com.cws.kanvas.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.VerticalAlignmentLine

actual class UiColumnScope(private val scope: ColumnScope) : ColumnScope {

    override fun Modifier.weight(
        weight: Float,
        fill: Boolean,
    ): Modifier {
        return scope.run { weight(weight, fill) }
    }

    override fun Modifier.align(alignment: Alignment.Horizontal): Modifier {
        return scope.run { align(alignment) }
    }

    override fun Modifier.alignBy(alignmentLine: VerticalAlignmentLine): Modifier {
        return scope.run { alignBy(alignmentLine) }
    }

    override fun Modifier.alignBy(alignmentLineBlock: (Measured) -> Int): Modifier {
        return scope.run { alignBy(alignmentLineBlock) }
    }

    actual fun UiModifier.weight(
        weight: Float,
        fill: Boolean,
    ): UiModifier {
        return UiModifier(modifier.weight(weight, fill))
    }

    actual fun UiModifier.align(alignment: Alignment.Horizontal): UiModifier {
        return UiModifier(modifier.align(alignment))
    }

    actual fun UiModifier.alignBy(alignmentLine: VerticalAlignmentLine): UiModifier {
        return UiModifier(modifier.alignBy(alignmentLine))
    }

    actual fun UiModifier.alignBy(alignmentLineBlock: (Measured) -> Int): UiModifier {
        return UiModifier(modifier.alignBy(alignmentLineBlock))
    }

}