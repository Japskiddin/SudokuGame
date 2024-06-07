package io.github.japskiddin.sudoku.core.game.model

data class BoardCell(
    val row: Int,
    val col: Int,
    var value: Int = 0,
    var isError: Boolean = false,
    var isLocked: Boolean = false,
)