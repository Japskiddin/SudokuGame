package io.github.japskiddin.sudoku.data.model

data class Board(
  val uid: Long,
  val initialBoard: String,
  val solvedBoard: String,
  val difficulty: Int,
  val type: Int,
)