package net.odiak.reversi

class ColorCounter {
    val array = Array(Color.values().size) { 0 }

    operator fun get(color: Color) = array[color.ordinal]

    operator fun set(color: Color, count: Int) {
        array[color.ordinal] = count
    }
}