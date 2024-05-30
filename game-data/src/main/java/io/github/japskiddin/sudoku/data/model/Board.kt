package io.github.japskiddin.sudoku.data.model

import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType

public data class Board(
  val uid: Long = ID_NONE,
  val initialBoard: String,
  val solvedBoard: String,
  val difficulty: GameDifficulty,
  val type: GameType,
) {
  public companion object {
    public const val ID_NONE: Long = 0L
  }
}