package com.cws.fmm

interface FastCollection {

    val size: Int

    fun release()

    fun clear()

}