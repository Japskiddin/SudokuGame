package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.database.model.BoardDBO

internal fun Board.toBoardDBO() = BoardDBO(
  uid = this.uid,
  initialBoard = this.initialBoard,
  solvedBoard = this.solvedBoard,
  difficulty = this.difficulty,
  type = this.type,
)

internal fun BoardDBO.toBoard() = Board(
  uid = this.uid,
  initialBoard = this.initialBoard,
  solvedBoard = this.solvedBoard,
  difficulty = this.difficulty,
  type = this.type,
)