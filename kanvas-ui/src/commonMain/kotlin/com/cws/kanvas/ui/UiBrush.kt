package com.cws.kanvas.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

interface UiBrush {

    companion object {

        fun linearGradient(
            vararg colorStops: Pair<Float, Color>,
            start: Offset = Offset.Zero,
            end: Offset = Offset.Infinite,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = UiLinearGradient(
            colors = List(colorStops.size) { i -> colorStops[i].second },
            stops = List(colorStops.size) { i -> colorStops[i].first },
            start = start,
            end = end,
            tileMode = tileMode
        )

        fun linearGradient(
            colors: List<Color>,
            start: Offset = Offset.Zero,
            end: Offset = Offset.Infinite,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = UiLinearGradient(
            colors = colors,
            start = start,
            end = end,
            tileMode = tileMode
        )

        fun horizontalGradient(
            colors: List<Color>,
            startX: Float = 0.0f,
            endX: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = linearGradient(colors, Offset(startX, 0.0f), Offset(endX, 0.0f), tileMode)

        fun horizontalGradient(
            vararg colorStops: Pair<Float, Color>,
            startX: Float = 0.0f,
            endX: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = linearGradient(
            *colorStops,
            start = Offset(startX, 0.0f),
            end = Offset(endX, 0.0f),
            tileMode = tileMode
        )

        fun verticalGradient(
            colors: List<Color>,
            startY: Float = 0.0f,
            endY: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = linearGradient(colors, Offset(0.0f, startY), Offset(0.0f, endY), tileMode)

        fun verticalGradient(
            vararg colorStops: Pair<Float, Color>,
            startY: Float = 0f,
            endY: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = linearGradient(
            *colorStops,
            start = Offset(0.0f, startY),
            end = Offset(0.0f, endY),
            tileMode = tileMode
        )

        fun radialGradient(
            vararg colorStops: Pair<Float, Color>,
            center: Offset = Offset.Unspecified,
            radius: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = UiRadialGradient(
            colors = List(colorStops.size) { i -> colorStops[i].second },
            stops = List(colorStops.size) { i -> colorStops[i].first },
            center = center,
            radius = radius,
            tileMode = tileMode
        )

        fun radialGradient(
            colors: List<Color>,
            center: Offset = Offset.Unspecified,
            radius: Float = Float.POSITIVE_INFINITY,
            tileMode: TileMode = TileMode.Clamp
        ): UiBrush = UiRadialGradient(
            colors = colors,
            center = center,
            radius = radius,
            tileMode = tileMode
        )

        fun sweepGradient(
            vararg colorStops: Pair<Float, Color>,
            center: Offset = Offset.Unspecified
        ): UiBrush = UiSweepGradient(
            colors = List(colorStops.size) { i -> colorStops[i].second },
            stops = List(colorStops.size) { i -> colorStops[i].first },
            center = center
        )

        fun sweepGradient(
            colors: List<Color>,
            center: Offset = Offset.Unspecified
        ): UiBrush = UiSweepGradient(
            colors = colors,
            center = center
        )

    }

}

@Immutable
data class UiSolidColor(val value: Color) : UiBrush

@Immutable
data class UiLinearGradient(
    val colors: List<Color>,
    val stops: List<Float>? = null,
    val start: Offset,
    val end: Offset,
    val tileMode: TileMode = TileMode.Clamp
) : UiBrush

@Immutable
data class UiRadialGradient(
    val colors: List<Color>,
    val stops: List<Float>? = null,
    val center: Offset,
    val radius: Float,
    val tileMode: TileMode = TileMode.Clamp
) : UiBrush

@Immutable
data class UiSweepGradient(
    val center: Offset,
    val colors: List<Color>,
    val stops: List<Float>? = null
) : UiBrush