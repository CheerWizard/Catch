package com.cws.nativeksp.math

@JvmInline
value class Color(val packed: Int) {

    companion object {
        const val SIZE_BYTES = Int.SIZE_BYTES * 4

        val White = Color(255, 255, 255)
        val Black = Color(0, 0, 0)
        val Red   = Color(255, 0, 0)
        val Green = Color(0, 255, 0)
        val Blue  = Color(0, 0, 255)
    }

    constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(pack(r, g, b, a))

    val r: Int get() = (packed shr 24) and 0xFF
    val g: Int get() = (packed shr 16) and 0xFF
    val b: Int get() = (packed shr 8) and 0xFF
    val a: Int get() = packed and 0xFF

    operator fun component1() = r
    operator fun component2() = g
    operator fun component3() = b
    operator fun component4() = a

    fun toVec4() = Vec4(r / 255f, g / 255f, b / 255f, a / 255f)

}

internal fun pack(r: Int, g: Int, b: Int, a: Int): Int {
    return (r and 0xFF shl 24) or
            (g and 0xFF shl 16) or
            (b and 0xFF shl 8)  or
            (a and 0xFF)
}