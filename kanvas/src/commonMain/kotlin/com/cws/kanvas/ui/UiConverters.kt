package com.cws.kanvas.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

val Color.hex: String get() {
    return toArgb().toHexString()
}