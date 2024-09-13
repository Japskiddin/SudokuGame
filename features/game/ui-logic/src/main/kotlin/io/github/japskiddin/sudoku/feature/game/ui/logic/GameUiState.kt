package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

public data class GameUiState(
    val board: ImmutableList<ImmutableList<BoardCell>>,
    val selectedCell: BoardCell
) {
    public companion object {
        public val Initial: GameUiState = GameUiState(
            board = List(9) { row ->
                List(9) { col ->
                    BoardCell(
                        row,
                        col,
                        0
                    )
                }.toImmutableList()
            }.toImmutableList(),
            selectedCell = BoardCell.Empty
        )
    }
}
