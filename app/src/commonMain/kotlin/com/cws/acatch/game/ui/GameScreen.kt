package com.cws.acatch.game.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.cws.acatch.game.GameLoop
import com.cws.kanvas.ui.KText

@Composable
fun GameScreen(
    gameLoop: GameLoop
) {
    val score by gameLoop.score

    val animateScoreScale by animateFloatAsState(
        targetValue = if (gameLoop.animateScore.value) 1.5f else 1f,
        animationSpec = tween(100),
        label = "animateScoreScale",
        finishedListener = {
            if (it == 1.5f) gameLoop.animateScore.value = false
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
//            .background(
//                brush = Brush.radialGradient(
//                    center = Offset(screenWidthPx / 2, screenHeightPx / 2),
//                    radius = 1000f,
//                    colors = listOf(Color.White, Color.LightGray, Color.Transparent)
//                ),
//            )
    ) {
        KText(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(animateScoreScale),
            text = score.value.toString(),
            style = TextStyle(
                fontSize = 60.sp,
                color = Color(1f, 1f, 1f, 1f)
            )
        )
    }
}