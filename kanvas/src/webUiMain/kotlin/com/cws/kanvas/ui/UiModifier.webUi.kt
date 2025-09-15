package com.cws.kanvas.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.transform
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.AttrBuilderContext

actual class UiModifier actual constructor() {

    var attrs: AttrBuilderContext<*>? = null
        private set

    constructor(attrs: AttrBuilderContext<*>?) : this() {
        this.attrs = attrs
    }

    actual fun alpha(alpha: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            opacity(alpha)
        }
    }

    actual fun background(color: Color, shape: UiShape) = UiModifier {
        attrs?.invoke(this)
        style {
            backgroundColor(rgba(color.red, color.green, color.blue, color.alpha))
            shape(shape)
        }
    }

    actual fun clip(shape: UiShape) = UiModifier {
        attrs?.invoke(this)
        style {
            shape(shape)
        }
    }

    actual fun fillMaxSize(fraction: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            val size = (100.0f * fraction).toInt()
            property("width", "$size%")
            property("height", "$size%")
        }
    }

    actual fun fillMaxWidth(fraction: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            val width = (100.0f * fraction).toInt()
            property("width", "$width%")
        }
    }

    actual fun fillMaxHeight(fraction: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            val height = (100.0f * fraction).toInt()
            property("height", "$height%")
        }
    }

    actual fun scale(scale: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            property("transform", "scale(${scale.toDouble()})")
        }
    }

    @OptIn(ExperimentalComposeWebApi::class)
    actual fun offset(x: Dp, y: Dp) = UiModifier {
        attrs?.invoke(this)
        style {
            transform {
                translate(x.value.px, y.value.px)
            }
        }
    }

    @OptIn(ExperimentalComposeWebApi::class)
    actual fun rotate(degree: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            transform {
                rotate(degree.deg)
            }
        }
    }

    actual fun clickable(
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        enabled: Boolean,
        onClickLabel: String?,
        role: Role?,
        onClick: () -> Unit
    ) = UiModifier {
        attrs?.invoke(this)
        onClick { onClick() }
    }

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
    ) = UiModifier {
        attrs?.invoke(this)
        onClick { onClick() }
        if (onDoubleClick != null) {
            onDoubleClick { onDoubleClick() }
        }
    }

    actual fun padding(
        top: Dp,
        bottom: Dp,
        start: Dp,
        end: Dp
    ) = UiModifier {
        attrs?.invoke(this)
        style {
            if (top > 0.dp) paddingTop(top.value.px)
            if (bottom > 0.dp) paddingBottom(bottom.value.px)
            if (start > 0.dp) paddingLeft(start.value.px)
            if (end > 0.dp) paddingRight(end.value.px)
        }
    }

    actual fun border(width: Dp, brush: UiBrush, shape: UiShape) = UiModifier {
        attrs?.invoke(this)
        style {
            border {
                width(width.value.px)
                style(LineStyle.Solid)
                brush(brush)
                shape(shape)
            }
        }
    }

}