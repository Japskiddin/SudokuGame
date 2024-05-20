package io.github.japskiddin.sudoku.data.model

import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType

data class Board(
  val uid: Long = 0,
  val initialBoard: String,
  val solvedBoard: String,
  val difficulty: GameDifficulty,
  val type: GameType,
)