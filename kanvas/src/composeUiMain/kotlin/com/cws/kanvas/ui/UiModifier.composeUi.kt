package com.cws.kanvas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual class UiModifier actual constructor() {

    var modifier: Modifier = Modifier
        private set

    constructor(modifier: Modifier = Modifier) : this() {
        this.modifier = modifier
    }

    actual fun alpha(alpha: Float) = UiModifier(modifier.alpha(alpha))

    actual fun background(color: Color, shape: UiShape) = UiModifier(modifier.background(color, shape.shape))

    actual fun background(brush: UiBrush, shape: UiShape) = UiModifier(modifier.background(brush.brush, shape.shape))

    actual fun clip(shape: UiShape) = UiModifier(modifier.clip(shape.shape))

    actual fun fillMaxSize(fraction: Float) = UiModifier(modifier.fillMaxSize(fraction))

    actual fun fillMaxWidth(fraction: Float) = UiModifier(modifier.fillMaxWidth(fraction))

    actual fun fillMaxHeight(fraction: Float) = UiModifier(modifier.fillMaxHeight(fraction))

    actual fun scale(scale: Float) = UiModifier(modifier.scale(scale))

    actual fun offset(x: Dp, y: Dp) = UiModifier(modifier.offset(x, y))

    actual fun rotate(degree: Float) = UiModifier(modifier.rotate(degree))

    actual fun clickable(
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        enabled: Boolean,
        onClickLabel: String?,
        role: Role?,
        onClick: () -> Unit
    ) = UiModifier(modifier.clickable(interactionSource, indication, enabled, onClickLabel, role, onClick))

    actual fun combinedClickable(
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        enabled: Boolean,
        onClickLabel: String?,
        role: Role?,
        onLongClickLabel: String?,
        onLongClick: (() -> Unit)?,
        onDoubleClick: (() -> Unit)?,
        hapticFeedbackEnabled: Boolean,
        onClick: () -> Unit
    ) = UiModifier(modifier)

    actual fun padding(
        top: Dp,
        bottom: Dp,
        start: Dp,
        end: Dp
    ) = UiModifier(modifier.padding(top = top, bottom = bottom, start = start, end = end))

    actual fun border(width: Dp, brush: UiBrush, shape: UiShape) = UiModifier(
        modifier.border(width, brush.brush, shape.shape)
    )

}