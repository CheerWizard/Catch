package com.cws.acatch.game.data

import com.cws.kmemory.IntArrayList

class GameGrid(
    width: Int,
    height: Int,
    val cellSize: Int
) {

    val cols = width / cellSize
    val rows = height / cellSize
    val cells = Array(rows * cols) { IntArrayList(0) }

    fun clear() {
        cells.forEach { it.clear() }
    }

    fun fill(entities: List<EntityData>) {
        entities.forEachIndexed { i, entity ->
            if (!entity.visible) return@forEachIndexed
            val col = col(entity.pos.x).coerceIn(0, cols - 1)
            val row = row(entity.pos.y).coerceIn(0, rows - 1)
            val cellIndex = row * cols + col
            cells[cellIndex].add(i)
        }
    }

    fun col(x: Float) = (x / cellSize).toInt()

    fun row(y: Float) = (y / cellSize).toInt()

}