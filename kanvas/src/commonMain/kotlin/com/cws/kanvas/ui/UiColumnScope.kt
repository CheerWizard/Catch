package com.cws.kanvas.ui

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.VerticalAlignmentLine

expect class UiColumnScope {
    fun UiModifier.weight(weight: Float, fill: Boolean = true): UiModifier
    fun UiModifier.align(alignment: Alignment.Horizontal): UiModifier
    fun UiModifier.alignBy(alignmentLine: VerticalAlignmentLine): UiModifier
    fun UiModifier.alignBy(alignmentLineBlock: (Measured) -> Int): UiModifier
}