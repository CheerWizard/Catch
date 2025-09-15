package com.cws.kanvas.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

expect class UiModifier() {

    fun alpha(alpha: Float): UiModifier

    fun background(color: Color, shape: UiShape = UiRectangleShape): UiModifier

    fun clip(shape: UiShape): UiModifier

    fun fillMaxSize(fraction: Float = 1f): UiModifier

    fun fillMaxWidth(fraction: Float = 1f): UiModifier

    fun fillMaxHeight(fraction: Float = 1f): UiModifier

    fun scale(scale: Float): UiModifier

    fun offset(x: Dp = 0.dp, y: Dp = 0.dp): UiModifier

    fun rotate(degree: Float): UiModifier

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

    fun padding(
        top: Dp = 0.dp,
        bottom: Dp = 0.dp,
        start: Dp = 0.dp,
        end: Dp = 0.dp
    ): UiModifier

    fun border(width: Dp, brush: UiBrush, shape: UiShape = UiRectangleShape): UiModifier

}

fun UiModifier.padding(all: Dp = 0.dp) = padding(all, all, all, all)

fun UiModifier.padding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): UiModifier = padding(top = vertical, bottom = vertical, start = horizontal, end = horizontal)

fun UiModifier.padding(
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
    start: Dp = 0.dp,
    end: Dp = 0.dp,
): UiModifier = padding(top = top, bottom = bottom, start = start, end = end)

fun UiModifier.border(width: Dp, color: Color, shape: UiShape = UiRectangleShape) = border(width, UiSolidColor(color), shape)

fun UiModifier.border(width: Dp, brush: UiBrush, shape: UiShape = UiRectangleShape) = border(width, brush, shape)