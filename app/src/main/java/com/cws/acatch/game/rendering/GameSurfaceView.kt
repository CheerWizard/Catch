package com.cws.acatch.game.rendering

import android.content.Context
import android.opengl.GLSurfaceView

class GameSurfaceView(
    context: Context,
    renderer: GameRenderer
) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(3)
        setRenderer(renderer)
    }

}