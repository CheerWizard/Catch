package com.cws.kanvas.ui

import androidx.compose.ui.Alignment

expect class UiBoxScope {
    fun UiModifier.align(alignment: Alignment): UiModifier
}