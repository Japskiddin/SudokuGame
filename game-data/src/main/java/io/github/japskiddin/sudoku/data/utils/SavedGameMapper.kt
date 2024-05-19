package io.github.japskiddin.sudoku.data.utils

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
  status = this.status,
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
  status = this.status,
)