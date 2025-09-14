package com.cws.kanvas.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role

actual class UiModifier actual constructor() {

    var modifier: Modifier = Modifier
        private set

    constructor(modifier: Modifier = Modifier) : this() {
        this.modifier = modifier
    }

    actual fun background(color: Color, shape: Shape) = UiModifier(modifier.background(color, shape))

    actual fun fillMaxSize(fraction: Float) = UiModifier(modifier.fillMaxSize(fraction))

    actual fun scale(scale: Float) = UiModifier(modifier.scale(scale))

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

}