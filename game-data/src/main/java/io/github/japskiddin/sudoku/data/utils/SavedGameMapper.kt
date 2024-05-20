package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.game.qqwing.GameStatus
import io.github.japskiddin.sudoku.data.model.SavedGame
import io.github.japskiddin.sudoku.database.model.SavedGameDBO

internal fun SavedGame.toSavedGameDBO() = SavedGameDBO(
  uid = this.uid,
  currentBoard = this.currentBoard,
  notes = this.notes,
  actions = this.actions,
  mistakes = this.mistakes,
  timer = this.timer,
  lastPlayed = this.lastPlayed,
  startedAt = this.startedAt,
  finishedAt = this.finishedAt,
  status = this.status.toInt(),
)

internal fun SavedGameDBO.toSavedGame() = SavedGame(
  uid = this.uid,
  currentBoard = this.currentBoard,
  notes = this.notes,
  actions = this.actions,
  mistakes = this.mistakes,
  timer = this.timer,
  lastPlayed = this.lastPlayed,
  startedAt = this.startedAt,
  finishedAt = this.finishedAt,
  status = this.status.toGameStatus(),
)

internal fun Int.toGameStatus() = when (this) {
  0 -> GameStatus.IN_PROGRESS
  1 -> GameStatus.FAILED
  2 -> GameStatus.COMPLETED
  else -> throw IncorrectGameStatusException("Incorrect game status value. Value must be between 0-2, found $this")
}

internal fun GameStatus.toInt() = when (this) {
  GameStatus.IN_PROGRESS -> 0
  GameStatus.FAILED -> 1
  GameStatus.COMPLETED -> 2
  else -> throw IncorrectGameStatusException("Incorrect game status value. Value must be ${GameStatus.entries}, found $this")
}

class IncorrectGameStatusException(message: String) : Exception(message)