package com.cws.acatch.graphics

import com.cws.kmemory.math.Vec2

@JvmInline
value class Vertices(val values: FloatArray) {

    companion object {
        val ATTRIBUTES = listOf(
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
    }

    fun setPos(i: Int, pos: Vec2) {
        values[i + 0] = pos.x
        values[i + Float.SIZE_BYTES * 1] = pos.y
    }

    fun pos(i: Int): Vec2 = Vec2(
        values[i + 0],
        values[i + Float.SIZE_BYTES * 1]
    )

    fun setUV(i: Int, uv: Vec2) {
        values[i + Float.SIZE_BYTES * 2] = uv.x
        values[i + Float.SIZE_BYTES * 3] = uv.y
    }

    fun uv(i: Int): Vec2 = Vec2(
        values[i + Float.SIZE_BYTES * 2],
        values[i + Float.SIZE_BYTES * 3]
    )

}