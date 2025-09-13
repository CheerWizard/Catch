package com.cws.acatch.game.rendering

import com.cws.acatch.game.data.Ball
import com.cws.kanvas.UniformBuffer

class BallBuffer : UniformBuffer(
    binding = 0,
    typeSize = Ball.SIZE_BYTES,
    count = 100,
)