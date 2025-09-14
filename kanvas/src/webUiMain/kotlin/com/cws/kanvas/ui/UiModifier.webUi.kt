package com.cws.kanvas.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.alignContent
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.dom.AttrBuilderContext

actual class UiModifier actual constructor() {

    var attrs: AttrBuilderContext<*>? = null
        private set

    constructor(attrs: AttrBuilderContext<*>?) : this() {
        this.attrs = attrs
    }

    actual fun background(color: Color, shape: Shape) = UiModifier {
        attrs?.invoke(this)
        style {
            backgroundColor(rgba(color.red, color.green, color.blue, color.alpha))
            // TODO: implement shapes
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

    actual fun scale(scale: Float) = UiModifier {
        attrs?.invoke(this)
        style {
            property("transform", "scale(${scale.toDouble()})")
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

}