package com.cws.acatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.cws.acatch.game.GameLoop
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.KanvasView
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private lateinit var gameLoop: GameLoop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanvasView(
                modifier = Modifier.fillMaxSize(),
                renderLoop = gameLoop
            ) {
                GameScreen(koinInject())
            }
        }
    }

}