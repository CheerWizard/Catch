package com.cws.kanvas.ui

import androidx.compose.ui.Alignment
import org.jetbrains.compose.web.css.AlignContent

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