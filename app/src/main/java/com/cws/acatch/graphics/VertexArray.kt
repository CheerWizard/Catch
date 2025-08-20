package com.cws.acatch.graphics

import android.opengl.GLES30.*

class VertexArray(
    private val attributes: List<VertexAttribute>
) {

    private val handle = IntArray(1)

    fun init() {
        glGenVertexArrays(1, handle, 0)

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
            glVertexAttribDivisor(
                attribute.location,
                if (attribute.enableInstancing) 1 else 0
            )
            attributeOffset += attribute.type.stride
        }
    }

    fun release() {
        attributes.forEach { attribute ->
            glDisableVertexAttribArray(attribute.location)
        }
        glDeleteVertexArrays(1, handle, 0)
    }

    fun bind() {
        glBindVertexArray(handle[0])
    }

}