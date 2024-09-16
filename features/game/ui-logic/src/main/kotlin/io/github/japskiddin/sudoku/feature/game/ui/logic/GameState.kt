package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.game.utils.BoardList
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class GameState(
    val initialBoard: BoardList,
    val solvedBoard: BoardList,
    val board: BoardList,
    val notes: ImmutableList<BoardNote>,
    val selectedCell: BoardCell
) {
    internal companion object {
        val Initial: GameState = GameState(
            board = List(9) { row ->
                List(9) { col ->
                    BoardCell(row, col)
                }
            },
            initialBoard = listOf(),
            solvedBoard = listOf(),
            notes = persistentListOf(),
            selectedCell = BoardCell.Empty
        )
    }
}
