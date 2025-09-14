package com.cws.kanvas.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

actual class UiBoxScope(private val scope: BoxScope): BoxScope {
    override fun Modifier.align(alignment: Alignment): Modifier = scope.run { return align(alignment) }
    override fun Modifier.matchParentSize(): Modifier = scope.run { return matchParentSize() }
    actual fun UiModifier.align(alignment: Alignment) = UiModifier(modifier.align(alignment))
}