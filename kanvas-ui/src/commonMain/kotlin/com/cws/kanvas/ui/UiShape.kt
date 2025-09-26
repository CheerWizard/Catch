package com.cws.kanvas.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface UiShape

object UiRectangleShape : UiShape

object UiCircleShape : UiShape

data class UiRoundedCornerShape(
    val topStart: Dp = 0.dp,
    val topEnd: Dp = 0.dp,
    val bottomEnd: Dp = 0.dp,
    val bottomStart: Dp = 0.dp
) : UiShape