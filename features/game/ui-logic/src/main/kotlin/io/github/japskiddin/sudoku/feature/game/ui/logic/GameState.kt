package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.game.utils.BoardList
import io.github.japskiddin.sudoku.core.game.utils.emptyBoardList
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote

internal data class GameState(
    val initialBoard: BoardList,
    val solvedBoard: BoardList,
    val board: BoardList,
    val notes: List<BoardNote>,
    val selectedCell: BoardCell,
    val actions: Int,
    val mistakes: Int,
    val timer: Long
) {
    internal companion object {
        val Initial: GameState = GameState(
            board = emptyBoardList,
            initialBoard = listOf(),
            solvedBoard = listOf(),
            notes = listOf(),
            selectedCell = BoardCell.Empty,
            actions = 0,
            mistakes = 0,
            timer = 0L
        )
    }
}
