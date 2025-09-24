package com.cws.kanvas.pipeline

import com.cws.kanvas.math.Vec2
import com.cws.kanvas.math.Vec3
import com.cws.kmemory.FastObject

@FastObject
data class _Vertex(
    val pos: Vec3,
    val uv: Vec2
)

val VERTEX_ATTRIBUTES = listOf(
    VertexAttribute(
        location = 0,
        type = VertexAttribute.Type.FLOAT2,
        enableInstancing = true
    ),
    VertexAttribute(
        location = 1,
        type = VertexAttribute.Type.FLOAT2,
        enableInstancing = true
    )
)