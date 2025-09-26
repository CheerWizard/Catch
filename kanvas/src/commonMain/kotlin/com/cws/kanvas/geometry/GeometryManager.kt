package com.cws.kanvas.geometry

import com.cws.kanvas.pipeline.IndexBuffer
import com.cws.kanvas.pipeline.VertexBuffer

typealias GeometryID = Int

class GeometryManager(
    private val vertexBuffer: VertexBuffer,
    private val indexBuffer: IndexBuffer
) {

    fun add(geometry: Geometry): GeometryID = 0

}