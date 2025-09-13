package com.cws.kmemory

interface FastCollection {

    val size: Int

    fun release()

    fun clear()

}