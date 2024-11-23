package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardImmutableList
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.emptyBoardList
import io.github.japskiddin.sudoku.core.model.toImmutable

public data class GameUiState(
    val board: BoardImmutableList,
    val selectedCell: BoardCell,
    val type: GameType,
    val difficulty: GameDifficulty,
    val actions: Int,
    val mistakes: Int,
    val time: Long
) {
    public companion object {
        public val Initial: GameUiState = GameUiState(
            board = emptyBoardList.toImmutable(),
            selectedCell = BoardCell.Empty,
            type = GameType.DEFAULT9X9,
            difficulty = GameDifficulty.INTERMEDIATE,
            actions = 0,
            mistakes = 0,
            time = 0L
        )
    }
}
