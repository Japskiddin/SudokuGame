package io.github.japskiddin.sudoku.data.model

data class SavedGame(
  val uid: Long,
  val currentBoard: String,
  val notes: String,
  val actions: Int,
  val mistakes: Int,
  val timer: Long,
  val lastPlayed: Long,
  val startedAt: Long,
  val finishedAt: Long,
  val status: Int,
)