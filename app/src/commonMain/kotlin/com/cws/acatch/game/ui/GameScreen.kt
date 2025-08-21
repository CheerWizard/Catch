package com.cws.acatch.game.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cws.acatch.game.GameManager
import com.cws.acatch.game.handleInput
import com.cws.acatch.game.rendering.GameRenderer
import com.cws.acatch.game.rendering.GameSurfaceView

@Composable
fun GameScreen(
    gameManager: GameManager
) {
    val density = LocalDensity.current

    val screenWidthDp = config.screenWidthDp.dp
    val screenHeightDp = config.screenHeightDp.dp
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    val screenHeightPx = with(density) { screenHeightDp.toPx() }

    var running by remember { mutableStateOf(true) }
    var tick by remember { mutableLongStateOf(0L) }

    val score by gameManager.score

    val animateScoreScale by animateFloatAsState(
        targetValue = if (gameManager.animateScore.value) 1.5f else 1f,
        animationSpec = tween(100),
        label = "animateScoreScale",
        finishedListener = {
            if (it == 1.5f) gameManager.animateScore.value = false
        }
    )

    LaunchedEffect(Unit) {
        withFrameMillis { time ->
            gameManager.onCreate(time)
            tick = time
        }
        while (running) {
            withFrameMillis { time ->
                gameManager.onUpdate(time)
                tick = time
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            running = false
            gameManager.onDestroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        center = Offset(screenWidthPx / 2, screenHeightPx / 2),
                        radius = 1000f,
                        colors = listOf(Color.White, Color.LightGray, Color.Transparent)
                    ),
                )
                .pointerInput(Unit) {
                    handleInput(gameManager)
                },
            factory = {
                GameSurfaceView(
                    context = context,
                    renderer = gameManager.renderer
                )
            }
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(animateScoreScale),
            text = score.value.toString(),
            style = TextStyle(
                fontSize = 60.sp,
                color = Color(score.color.packed)
            )
        )
    }
}

@Preview
@Composable
fun Preview_Game() {
    GameScreen(
        gameManager = GameManager(
            context = LocalContext.current,
            width = LocalConfiguration.current.screenWidthDp.toFloat(),
            height = LocalConfiguration.current.screenHeightDp.toFloat(),
            renderer = GameRenderer()
        )
    )
}