package com.cws.kanvas.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role

expect class UiModifier() {

    fun background(color: Color, shape: Shape = RectangleShape): UiModifier

    fun fillMaxSize(fraction: Float = 1f): UiModifier

    fun scale(scale: Float): UiModifier

    fun align(alignment: Alignment): UiModifier

    fun clickable(
        interactionSource: MutableInteractionSource? = null,
        indication: Indication? = null,
        enabled: Boolean = true,
        onClickLabel: String? = null,
        role: Role? = null,
        onClick: () -> Unit
    ): UiModifier

    fun combinedClickable(
        interactionSource: MutableInteractionSource? = null,
        indication: Indication? = null,
        enabled: Boolean = true,
        onClickLabel: String? = null,
        role: Role? = null,
        onLongClickLabel: String? = null,
        onLongClick: (() -> Unit)? = null,
        onDoubleClick: (() -> Unit)? = null,
        hapticFeedbackEnabled: Boolean = true,
        onClick: () -> Unit
    ): UiModifier

}