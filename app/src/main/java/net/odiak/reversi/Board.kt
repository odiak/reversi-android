package net.odiak.reversi

class Board(val size: Int) {
    val rawBoardSize = size * size

    val rawBoard = Array(rawBoardSize) { Color.EMPTY }

    operator fun get(x: Int, y: Int): Color? {
        if (x < 0 || x >= size || y < 0 || y >= size) return null
        return rawBoard[x + y * size]
    }

    operator fun get(point: Point): Color? = this[point.x, point.y]

    operator fun set(x: Int, y: Int, color: Color) {
        if (x < 0 || x >= size || y < 0 || y >= size) return
        rawBoard[x + y * size] = color
    }

    operator fun set(point: Point, color: Color) = {
        this[point.x, point.y] = color
    }

    fun forEach(callback: (x: Int, y: Int, color: Color) -> Unit) {
        for (x in 0..(size - 1)) {
            for (y in 0..(size - 1)) {
                callback(x, y, rawBoard[x + y * size])
            }
        }
    }

    fun empty() {
        forEach { x, y, color ->
            this[x, y] = Color.EMPTY
        }
    }
}