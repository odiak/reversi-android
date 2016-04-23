package net.odiak.reversi

enum class Direction(val flag: Int, val x: Int, val y: Int) {
    LEFT       (1.shl(0), -1,  0),
    UPPER_LEFT (1.shl(1), -1, -1),
    UPPER      (1.shl(2),  0, -1),
    UPPER_RIGHT(1.shl(3),  1, -1),
    RIGHT      (1.shl(4),  1,  0),
    LOWER_RIGHT(1.shl(5),  1,  1),
    LOWER      (1.shl(6),  0,  1),
    LOWER_LEFT (1.shl(7), -1,  1);
}