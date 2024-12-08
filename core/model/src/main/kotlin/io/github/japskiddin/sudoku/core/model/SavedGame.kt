package io.github.japskiddin.sudoku.core.model

public data class SavedGame(
    val uid: Long,
    val board: String,
    val notes: String,
    val actions: Int,
    val mistakes: Int,
    val time: Long,
    val lastPlayed: Long,
    val startedTime: Long,
    val finishedTime: Long,
    val status: GameStatus
) {
    public companion object {
        public val EMPTY: SavedGame = SavedGame(
            uid = Board.ID_EMPTY,
            board = "",
            notes = "",
            actions = 0,
            mistakes = 0,
            time = 0L,
            lastPlayed = 0L,
            startedTime = 0L,
            finishedTime = 0L,
            status = GameStatus.IN_PROGRESS
        )
    }
}

public fun SavedGame.isCurrent(): Boolean = uid != Board.ID_EMPTY && status == GameStatus.IN_PROGRESS
