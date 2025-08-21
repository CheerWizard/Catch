package com.cws.kanvas

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
            type = Kanvas.UINT,
            stride = UInt.SIZE_BYTES
        ),
        FLOAT2(
            size = 2,
            type = Kanvas.FLOAT,
            stride = Float.SIZE_BYTES
        )
    }

}