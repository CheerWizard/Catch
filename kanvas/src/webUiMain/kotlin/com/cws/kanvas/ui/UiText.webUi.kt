package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
actual fun UiText(
    modifier: UiModifier,
    text: String,
    style: TextStyle
) {
    Div(
        attrs = {
            modifier.toAttrs()?.invoke(this)
            style {
                color(rgba(style.color.red, style.color.green, style.color.blue, style.color.alpha))
                fontSize(style.fontSize.value.px)
                fontFamily("PressStart2P, sans-serif")
                textAlign(style.textAlign.cssValue)
            }
        }
    ) {
        Text(text)
    }
}

val TextAlign.cssValue: String get() = when (this) {
    TextAlign.Start, TextAlign.Left -> "left"
    TextAlign.End, TextAlign.Right -> "right"
    TextAlign.Center -> "center"
    else -> "left"
}