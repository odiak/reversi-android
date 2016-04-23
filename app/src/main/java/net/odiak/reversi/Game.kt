package net.odiak.reversi

import net.odiak.reversi.Color.*

class Game {
    val size = 8
    val board = Board(size)

    val maxTurns = size * size - 4

    val updateLog = Array(maxTurns + 1) { mutableListOf<Disc>() }
    val movablePoint = Array(maxTurns + 1) { mutableListOf<Point>() }
    val movableDirs = Array(maxTurns + 1) { Array(size) { Array(size) { 0 } } }

    var currentColor = EMPTY

    val discs = ColorCounter()

    var turns = 0
        private set

    val currentMovablePoints: MutableList<Point>
        get() = movablePoint[turns]

    fun init() {
        board.empty()
        board[3, 3] = BLACK
        board[3, 4] = WHITE
        board[4, 3] = WHITE
        board[4, 4] = BLACK

        discs[EMPTY] = size * size - 4
        discs[BLACK] = 2
        discs[WHITE] = 2

        turns = 0
        currentColor = BLACK

        updateLog.forEach { it.clear() }
        movablePoint.forEach { it.clear() }

        initMovable()

        (1..2).endInclusive
    }

    private fun initMovable() {
        for (x in 0..(size - 1)) {
            for (y in 0..(size - 1)) {
                val disc = Disc(x, y, currentColor)
                val mobility = checkMobility(disc)
                if (mobility != 0) {
                    movablePoint[turns].add(disc)
                }
                movableDirs[turns][x][y] = mobility
            }
        }
    }

    private fun checkMobility(disc: Disc): Int {
        if (board[disc] != Color.EMPTY || disc.color.isEmpty) return 0

        val color = disc.color
        val oppColor = color.opposite

        return Direction.values().fold(0) { acc, dir ->
            val dx = dir.x
            val dy = dir.y
            var x: Int = disc.x + dx
            var y: Int = disc.y + dy

            if (board[x, y] != oppColor) return@fold acc
            x += dx
            y += dy

            while (board[x, y] == oppColor) {
                x += dx
                y += dy
            }

            if (board[x, y] != color) return@fold acc

            return@fold acc.or(dir.flag)
        }
    }

    fun move(p: Point): Boolean {
        if (p.x < 0 || p.x >= size || p.y < 0 || p.y >= size) return false;
        if (movableDirs[turns][p.x][p.y] == 0) return false

        turnOver(p)

        turns += 1

        currentColor = currentColor.opposite

        initMovable()

        return true
    }

    private fun turnOver(p: Point) {
        val d = movableDirs[turns][p.x][p.y];
        val updates = mutableListOf<Disc>()
        val oppColor = currentColor.opposite

        updates.add(Disc(p.x, p.y, currentColor))
        board[p.x, p.y] = currentColor

        Direction.values().forEach { dir ->
            if (d.and(dir.flag) == 0) return@forEach

            var x = p.x + dir.x
            var y = p.y + dir.y

            while (board[x, y] == oppColor) {
                updates.add(Disc(x, y, currentColor))
                board[x, y] = currentColor
                x += dir.x
                y += dir.y
            }
        }

        val diff = updates.size

        discs[currentColor] += diff
        discs[currentColor.opposite] -= diff - 1
        discs[EMPTY] -= 1

        updateLog[turns] = updates
    }

    fun isGameOver(): Boolean {
        if (turns == maxTurns) return true

        if (movablePoint[turns].size != 0) return false

        for (x in 0..(size - 1)) {
            for (y in 0..(size - 1)) {
                if (checkMobility(Disc(x, y, currentColor.opposite)) != 0) return false
            }
        }
        return true
    }

    fun pass(): Boolean {
        if (!movablePoint[turns].isEmpty()) return false
        if (isGameOver()) return false

        currentColor = currentColor.opposite

        initMovable()

        return true
    }

    fun isMovableTo(x: Int, y: Int) = movableDirs[turns][x][y] != 0

    fun undo(): Boolean {
        if (turns == 0) return false

        currentColor = currentColor.opposite

        val updates = updateLog[turns - 1]

        if (updates.isEmpty()) {
            movablePoint[turns].clear()
            for (x in 0..(size - 1)) {
                for (y in 0..(size - 1)) {
                    movableDirs[turns][x][y] = 0
                }
            }
        } else {
            turns -= 1
            val firstUpdate = updates[0]
            board[firstUpdate.x, firstUpdate.y] = EMPTY

            for (i in 1..(updates.size - 1)) {
                val update = updates[i]
                board[update.x, update.y] = currentColor.opposite
            }

            val diff = updates.size
            discs[currentColor] -= diff
            discs[currentColor.opposite] += diff - 1
            discs[EMPTY] -= 1
        }

        return true
    }
}