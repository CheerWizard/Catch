package com.cws.kanvas

interface Renderer {
    fun init()
    fun release()
    fun render()
}