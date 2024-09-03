package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

public data class GameState(
    val board: ImmutableList<ImmutableList<BoardCell>>,
    val initialBoard: ImmutableList<ImmutableList<BoardCell>>,
    val solvedBoard: ImmutableList<ImmutableList<BoardCell>>,
    val notes: ImmutableList<BoardNote>,
    val selectedCell: BoardCell
) {
    public companion object {
        public val Initial: GameState = GameState(
            board = List(9) { row ->
                List(9) { col ->
                    BoardCell(
                        row,
                        col,
                        0
                    )
                }.toImmutableList()
            }.toImmutableList(),
            initialBoard = emptyList<ImmutableList<BoardCell>>().toImmutableList(),
            solvedBoard = emptyList<ImmutableList<BoardCell>>().toImmutableList(),
            notes = persistentListOf(),
            selectedCell = BoardCell.Empty
        )
    }
}
