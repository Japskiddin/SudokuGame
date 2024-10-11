package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.game.utils.BoardImmutableList
import io.github.japskiddin.sudoku.core.game.utils.emptyBoardList
import io.github.japskiddin.sudoku.core.game.utils.toImmutable
import io.github.japskiddin.sudoku.core.model.BoardCell

public data class GameUiState(
    val board: BoardImmutableList,
    val selectedCell: BoardCell
) {
    public companion object {
        public val Initial: GameUiState = GameUiState(
            board = emptyBoardList.toImmutable(),
            selectedCell = BoardCell.Empty
        )
    }
}
