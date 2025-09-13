package com.cws.kanvas

val Float.fps get() = 1 / (this / 1_000.0f)