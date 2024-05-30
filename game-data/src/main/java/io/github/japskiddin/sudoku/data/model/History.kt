package io.github.japskiddin.sudoku.data.model

import java.util.Date

public data class History(
  val id: Long,
  val date: Date,
  val level: GameLevel,
)