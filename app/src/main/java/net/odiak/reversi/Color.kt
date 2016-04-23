package net.odiak.reversi

enum class Color {
    EMPTY,
    WHITE,
    BLACK;

    val isEmpty: Boolean
        get() = this == EMPTY

    val opposite: Color
        get() = when (this) {
            EMPTY -> EMPTY
            BLACK -> WHITE
            WHITE -> BLACK
        }

    val sign: Int
        get() = when (this) {
            EMPTY -> 0
            BLACK -> 1
            WHITE -> -1
        }
}

class Cons<T>(val head: T, val tail: Cons<T>?) {
}

fun <T> Cons<T>?.size(): Int {
    if (this == null) return 0
    return tail.size() + 1
}

fun <T> Cons<T>?.each(callback: (x: T) -> Unit) {
    if (this == null) return
    callback(head)
    tail.each(callback)
}