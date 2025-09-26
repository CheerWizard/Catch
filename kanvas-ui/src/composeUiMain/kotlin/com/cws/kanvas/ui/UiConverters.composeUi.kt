package com.cws.kanvas.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor

val UiShape.shape: Shape get() {
    return when (this) {
        is UiRectangleShape -> RectangleShape
        is UiCircleShape -> CircleShape
        is UiRoundedCornerShape -> RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )
        else -> RectangleShape
    }
}

val UiBrush.brush: Brush get() {
    return when (this) {
        is UiSolidColor -> SolidColor(value)
        is UiLinearGradient -> Brush.linearGradient(
            colors = colors,
            start = start,
            end = end,
            tileMode = tileMode,
        )
        is UiRadialGradient -> Brush.radialGradient(
            colors = colors,
            radius = radius,
            center = center,
            tileMode = tileMode
        )
        is UiSweepGradient -> Brush.sweepGradient(
            colors = colors,
            center = center
        )
        else -> Brush.linearGradient()
    }
}