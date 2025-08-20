package com.cws.acatch.graphics

import android.opengl.GLES30.*

data class VertexAttribute(
    val location: Int,
    val type: Type,
    val enableInstancing: Boolean
) {

    enum class Type(
        val size: Int,
        val type: Int,
        val stride: Int
    ) {
        UINT(
            size = 1,
            type = GL_UNSIGNED_INT,
            stride = UInt.SIZE_BYTES
        ),
        FLOAT2(
            size = 2,
            type = GL_FLOAT,
            stride = Float.SIZE_BYTES
        )
    }

}