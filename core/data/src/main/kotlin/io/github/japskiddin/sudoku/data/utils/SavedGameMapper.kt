package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.common.IncorrectGameStatusException
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO

internal fun SavedGame.toSavedGameDBO() = SavedGameDBO(
    uid = this.uid,
    board = this.board,
    notes = this.notes,
    actions = this.actions,
    mistakes = this.mistakes,
    time = this.time,
    lastPlayed = this.lastPlayed,
    startedTime = this.startedTime,
    finishedTime = this.finishedTime,
    status = this.status.toInt()
)

internal fun SavedGameDBO.toSavedGame() = SavedGame(
    uid = this.uid,
    board = this.board,
    notes = this.notes,
    actions = this.actions,
    mistakes = this.mistakes,
    time = this.time,
    lastPlayed = this.lastPlayed,
    startedTime = this.startedTime,
    finishedTime = this.finishedTime,
    status = this.status.toGameStatus()
)

internal fun Int.toGameStatus() = when (this) {
    0 -> GameStatus.IN_PROGRESS
    1 -> GameStatus.FAILED
    2 -> GameStatus.COMPLETED
    else -> throw IncorrectGameStatusException(
        "Incorrect game status value. Value must be between 0-2, found $this"
    )
}

internal fun GameStatus.toInt() = when (this) {
    GameStatus.IN_PROGRESS -> 0
    GameStatus.FAILED -> 1
    GameStatus.COMPLETED -> 2
}
