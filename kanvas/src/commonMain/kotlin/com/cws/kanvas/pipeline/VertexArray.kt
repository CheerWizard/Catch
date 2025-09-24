package com.cws.kanvas.pipeline

import com.cws.kanvas.core.Kanvas
import com.cws.kanvas.core.VertexArrayID

class VertexArray(
    private val attributes: List<VertexAttribute>
) {

    private lateinit var handle: VertexArrayID

    fun init() {
        handle = Kanvas.vertexArrayInit()
        Kanvas.vertexArrayEnableAttributes(attributes)
    }

    fun release() {
        Kanvas.vertexArrayDisableAttributes(attributes)
        Kanvas.vertexArrayRelease(handle)
    }

    fun bind() {
        Kanvas.vertexArrayBind(handle)
    }

}