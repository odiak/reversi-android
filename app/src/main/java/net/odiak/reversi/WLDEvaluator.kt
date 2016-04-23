package net.odiak.reversi

class WLDEvaluator : Evaluator {
    val WIN = 1
    val LOSE = -1
    val DRAW = 0

    override fun evaluate(game: Game): Int {
        val color = game.currentColor
        return compare(game.discs[color], game.discs[color.opposite])
    }

    private fun compare(a: Int, b: Int): Int {
        if (a > b) {
            return WIN
        } else if (a < b) {
            return LOSE
        } else {
            return DRAW
        }
    }
}