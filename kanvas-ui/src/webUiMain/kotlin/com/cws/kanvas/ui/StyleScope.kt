package com.cws.kanvas.ui

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import kotlin.math.PI
import kotlin.math.atan2

fun StyleScope.shape(shape: UiShape) {
    when (shape) {
        is UiCircleShape -> borderRadius(50.percent)
        is UiRoundedCornerShape -> borderRadius(
            topLeft = shape.topStart.value.px,
            topRight = shape.topEnd.value.px,
            bottomLeft = shape.bottomStart.value.px,
            bottomRight = shape.bottomEnd.value.px,
        )
    }
}

fun StyleScope.brush(brush: UiBrush) {
    when (brush) {
        is UiSolidColor -> color(brush.value.cssColor)
        is UiLinearGradient -> {
            val params = StringBuilder()
            val dx = brush.end.x - brush.start.x
            val dy = brush.end.y - brush.start.y
            val radians = atan2(dy, dx)
            var degrees = radians * 180 / PI
            if (degrees < 0) {
                degrees += 360.0
            }
            params.append("${degrees.toInt()}deg, ")
            params.appendBrushColors(brush.colors, brush.stops)
            backgroundImage("linear-gradient($params)")
        }
        is UiRadialGradient -> {
            val params = StringBuilder()
            params.append("circle ${brush.radius.px} at ${brush.center.x.px} ${brush.center.y.px}, ")
            params.appendBrushColors(brush.colors, brush.stops)
            backgroundImage("radial-gradient($params)")
        }
        is UiSweepGradient -> {
            val params = StringBuilder()
            params.append("from 0deg at ${brush.center.x.px} ${brush.center.y.px}, ")
            params.appendBrushColors(brush.colors, brush.stops)
            background("conic-gradient($params)")
        }
    }
}

fun StringBuilder.appendBrushColors(colors: List<Color>, stops: List<Float>?) {
    colors.forEachIndexed { i, color ->
        append(color.hex)
        stops?.getOrNull(i)?.let { stop ->
            append(" ${stop.percent}")
        }
        if (i != colors.lastIndex) {
            append(", ")
        }
    }
}