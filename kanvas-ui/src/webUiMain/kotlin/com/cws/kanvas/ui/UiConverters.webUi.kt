package com.cws.kanvas.ui

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgba

val Alignment.alignContent: AlignContent get() {
    return when (this) {
        Alignment.Center -> AlignContent.Center
        Alignment.TopStart, Alignment.TopEnd -> AlignContent.FlexStart
        Alignment.BottomStart, Alignment.BottomEnd -> AlignContent.FlexEnd
        Alignment.Start -> AlignContent.Start
        Alignment.End -> AlignContent.End
        else -> AlignContent.Stretch
    }
}

val Color.cssColor: CSSColorValue get() {
    return rgba(red, green, blue, alpha)
}

val TextAlign.cssTextAlign: String get() = when (this) {
    TextAlign.Start, TextAlign.Left -> "left"
    TextAlign.End, TextAlign.Right -> "right"
    TextAlign.Center -> "center"
    else -> "left"
}