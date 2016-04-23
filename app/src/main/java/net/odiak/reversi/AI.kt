package net.odiak.reversi

class AI(val game: Game) {

    val presearchDepth = 3
    val normalSearchDepth = 10;
    val wldSearchDepth = 15;
    val perfectSearchDepth = 13;

    fun move() {
        val movablePoints = game.currentMovablePoints

        if (movablePoints.isEmpty()) {
            game.pass()
            return
        }

        if (movablePoints.size == 1) {
            game.move(movablePoints[0])
            return;
        }

        val limit: Int
        val evaluator: Evaluator
        val remainingTurns = game.maxTurns - game.turns

        if (remainingTurns <= wldSearchDepth) {
            limit = Int.MAX_VALUE
            if (remainingTurns <= perfectSearchDepth) {
                evaluator = PerfectEvaluator()
            } else {
                evaluator = WLDEvaluator()
            }
        } else {
            limit = normalSearchDepth
            evaluator = MidEvaluator()
        }

        val sortedMovablePoints = sortMovablePoints(movablePoints, evaluator, presearchDepth)

        var alpha = Int.MIN_VALUE
        val beta = Int.MAX_VALUE

        val point = sortedMovablePoints.reduce { acc, p ->
            game.move(p)
            val eval = -alphaBeta(evaluator, limit - 1, -beta, -alpha)
            game.undo()

            if (eval > alpha) {
                alpha = eval
                p
            } else {
                acc
            }
        }

        game.move(point)
    }

    fun sortMovablePoints(movablePoints: MutableList<Point>, evaluator: Evaluator, limit: Int):
            List<Point> {
        val moves = mutableListOf<Move>()

        for (p in movablePoints) {
            game.move(p)
            val eval = -alphaBeta(evaluator, limit - 1, Int.MIN_VALUE, Int.MAX_VALUE)
            game.undo()
            moves.add(Move(p.x, p.y, eval))
        }

        moves.sortByDescending(Move::eval)

        return moves
    }

    fun alphaBeta(evaluator: Evaluator, limit: Int, alpha: Int, beta: Int): Int {
        if (game.isGameOver() || limit == 0) {
            return evaluator.evaluate(game)
        }

        val movablePoints = game.currentMovablePoints

        if (movablePoints.isEmpty()) {
            game.pass()
            val score = -alphaBeta(evaluator, limit, -beta, -alpha)
            game.undo()
            return score
        }



        return movablePoints.fold(alpha) { alpha, p ->
            game.move(p)
            val score = -alphaBeta(evaluator, limit - 1, -beta, -alpha)

            if (score >= beta) return score

            Math.max(alpha, score)
        }
    }
}