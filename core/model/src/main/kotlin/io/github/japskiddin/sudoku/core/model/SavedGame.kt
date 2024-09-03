package io.github.japskiddin.sudoku.core.model

public data class SavedGame(
    val uid: Long = ID_NONE,
    val currentBoard: String,
    val notes: String,
    val actions: Int,
    val mistakes: Int,
    val timer: Long,
    val lastPlayed: Long,
    val startedAt: Long,
    val finishedAt: Long,
    val status: GameStatus
) {
    public companion object {
        public const val ID_NONE: Long = 0L
    }
}
