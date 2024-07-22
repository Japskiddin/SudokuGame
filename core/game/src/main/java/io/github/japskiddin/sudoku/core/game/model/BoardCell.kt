package io.github.japskiddin.sudoku.core.game.model

public data class BoardCell(
    val row: Int,
    val col: Int,
    var value: Int = 0,
    var isError: Boolean = false,
    var isLocked: Boolean = false
) {
    public companion object {
        public val Empty: BoardCell = BoardCell(-1, -1)
    }
}

public fun BoardCell.isEmpty(): Boolean = this == BoardCell.Empty
