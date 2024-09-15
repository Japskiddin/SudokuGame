package io.github.japskiddin.sudoku.core.model

public data class SavedGame(
    val uid: Long = ID_NONE,
    val board: String,
    val notes: String,
    val actions: Int,
    val mistakes: Int,
    val timer: Long,
    val lastPlayed: Long,
    val startedTime: Long,
    val finishedTime: Long,
    val status: GameStatus
) {
    public companion object {
        public const val ID_NONE: Long = 0L
    }
}
