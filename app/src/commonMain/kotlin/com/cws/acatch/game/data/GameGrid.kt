package com.cws.acatch.game.data

import com.cws.kanvas.math.Vec2
import com.cws.kmemory.FastList
import com.cws.kmemory.collections.IntList

class GameGrid(
    width: Int,
    height: Int,
    val cellSize: Int
) {

    val cols = width / cellSize
    val rows = height / cellSize
    val cells = Array(rows * cols) { IntList(0) }

    fun clear() {
        cells.forEach { it.clear() }
    }

    fun fill(
        entities: FastList,
        visible: (Int) -> Boolean,
        pos: (Int) -> Vec2
    ) {
        for (i in 0..<entities.size step entities.typeSize) {
            if (!visible(i)) continue
            val pos = pos(i)
            val col = col(pos.x).coerceIn(0, cols - 1)
            val row = row(pos.y).coerceIn(0, rows - 1)
            val cellIndex = row * cols + col
            cells[cellIndex].add(i)
        }
    }

    fun col(x: Float) = (x / cellSize).toInt()

    fun row(y: Float) = (y / cellSize).toInt()

}