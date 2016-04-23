package net.odiak.reversi

class PerfectEvaluator : Evaluator {
    override fun evaluate(game: Game): Int {
        val color = game.currentColor
        return game.discs[color] - game.discs[color.opposite]
    }
}