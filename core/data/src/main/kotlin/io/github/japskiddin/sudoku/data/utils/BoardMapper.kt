package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.database.entities.BoardDBO

internal fun Board.toBoardDBO() =
    BoardDBO(
        uid = this.uid,
        board = this.board,
        solvedBoard = this.solvedBoard,
        difficulty = this.difficulty.toInt(),
        type = this.type.toInt()
    )

internal fun BoardDBO.toBoard() =
    Board(
        uid = this.uid,
        board = this.board,
        solvedBoard = this.solvedBoard,
        difficulty = this.difficulty.toGameDifficulty(),
        type = this.type.toGameType()
    )
