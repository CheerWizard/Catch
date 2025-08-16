package com.cws.acatch.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cws.acatch.game.GameManager
import com.cws.acatch.game.GameRenderer
import com.cws.acatch.game.ui.GameScreen

class MainActivity : ComponentActivity() {

    private lateinit var gameManager: GameManager

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

            if (!::gameManager.isInitialized) {
                gameManager = GameManager(
                    context = applicationContext,
                    width = screenWidthPx,
                    height = screenHeightPx,
                    renderer = GameRenderer()
                )
            }

            GameScreen(
                gameManager = gameManager
            )
        }
    }

}