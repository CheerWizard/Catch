package com.cws.acatch.graphics

import android.opengl.GLES30.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexArray(
    private val attributes: List<VertexAttribute>
) {

    private val handle = ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer()

    fun init() {
        glGenVertexArrays(1, handle)

        var attributeOffset = 0
        attributes.forEach { attribute ->
            glEnableVertexAttribArray(attribute.location)
            glVertexAttribPointer(
                attribute.location,
                attribute.type.size,
                attribute.type.type,
                false,
                attribute.type.stride,
                attributeOffset
            )
            attributeOffset += attribute.type.stride
        }
    }

    fun release() {
        attributes.forEach { attribute ->
            glDisableVertexAttribArray(attribute.location)
        }

        glDeleteVertexArrays(1, handle)
    }

    fun bind() {
        glBindVertexArray(handle.get())
    }

}