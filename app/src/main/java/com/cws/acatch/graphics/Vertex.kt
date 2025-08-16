package com.cws.acatch.graphics

import androidx.compose.ui.geometry.Offset

data class Vertex(
    val position: Offset
) {

    companion object {
        val ATTRIBUTES = listOf(
            VertexAttribute(
                location = 0,
                type = VertexAttribute.Type.FLOAT2
            )
        )
    }

}