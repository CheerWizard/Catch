package com.cws.kanvas.event

interface EventListener {
    fun onWindowResized(width: Int, height: Int) = Unit
    fun onWindowMoved(x: Int, y: Int) = Unit
    fun onKeyPressed(code: KeyCode, hold: Boolean) = Unit
    fun onKeyReleased(code: KeyCode) = Unit
    fun onKeyTyped(char: Char) = Unit
    fun onMousePressed(code: MouseCode, hold: Boolean) = Unit
    fun onMouseReleased(code: MouseCode) = Unit
    fun onMouseMove(x: Double, y: Double) = Unit
    fun onMouseScroll(x: Double, y: Double) = Unit
    fun onTapPressed(x: Float, y: Float) = Unit
    fun onTapReleased(x: Float, y: Float) = Unit
}