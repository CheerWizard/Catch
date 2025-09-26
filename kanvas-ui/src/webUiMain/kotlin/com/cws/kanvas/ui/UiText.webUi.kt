package com.cws.kanvas.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.px
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
            modifier.attrs?.invoke(this)
            style {
                color(style.color.cssColor)
                fontSize(style.fontSize.value.px)
                fontFamily("PressStart2P, sans-serif")
                textAlign(style.textAlign.cssTextAlign)
            }
        }
    ) {
        Text(text)
    }
}