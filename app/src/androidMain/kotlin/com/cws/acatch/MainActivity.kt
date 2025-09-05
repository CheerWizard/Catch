package com.cws.acatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cws.acatch.game.GameLoop
import com.cws.kanvas.input.InputSensorManager
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.KanvasEntryPoint
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private lateinit var gameLoop: GameLoop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val config = LocalConfiguration.current
            val density = LocalDensity.current

            val screenWidthDp = config.screenWidthDp.dp
            val screenHeightDp = config.screenHeightDp.dp
            val screenWidthPx = with(density) { screenWidthDp.toPx() }
            val screenHeightPx = with(density) { screenHeightDp.toPx() }

            if (!::gameLoop.isInitialized) {
                gameLoop = GameLoop(
                    x = 0,
                    y = 0,
                    width = screenWidthPx.roundToInt(),
                    height = screenHeightPx.roundToInt(),
                    title = "",
                    inputSensorManager = InputSensorManager(applicationContext)
                )
            }

            KanvasEntryPoint(
                modifier = Modifier.fillMaxSize(),
                renderLoop = gameLoop
            ) {
                GameScreen(
                    gameLoop = gameLoop
                )
            }
        }
    }

}