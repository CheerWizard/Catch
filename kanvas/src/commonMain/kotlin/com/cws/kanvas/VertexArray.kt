package com.cws.kanvas

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