package io.github.japskiddin.sudoku.core.model

public data class Board(
    val uid: Long = ID_NONE,
    val initialBoard: String,
    val solvedBoard: String,
    val difficulty: GameDifficulty,
    val type: GameType
) {
    public companion object {
        public const val ID_NONE: Long = 0L
    }
}
