package com.cws.acatch.game.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.cws.acatch.game.GameLoop
import com.cws.kanvas.ui.UiBox
import com.cws.kanvas.ui.UiBrush
import com.cws.kanvas.ui.UiModifier
import com.cws.kanvas.ui.UiText
import org.koin.compose.koinInject

@Composable
fun GameScreen() {
    val gameLoop: GameLoop = koinInject()

    val score by gameLoop.score

    val animateScoreScale by animateFloatAsState(
        targetValue = if (gameLoop.animateScore.value) 1.5f else 1f,
        animationSpec = tween(100),
        label = "animateScoreScale",
        finishedListener = {
            if (it == 1.5f) gameLoop.animateScore.value = false
        }
    )

    UiBox(
        modifier = UiModifier()
            .fillMaxSize()
            .background(
                brush = UiBrush.radialGradient(
                    colors = listOf(Color.White, Color.LightGray, Color.Transparent),
                    center = Offset(gameLoop.width.toFloat() / 2, gameLoop.height.toFloat() / 2),
                    radius = gameLoop.height.toFloat() / 2,
                )
            )
    ) {
        UiText(
            modifier = UiModifier()
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