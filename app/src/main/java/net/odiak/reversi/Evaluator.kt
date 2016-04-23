package net.odiak.reversi

interface Evaluator {
    fun evaluate(game: Game): Int
}