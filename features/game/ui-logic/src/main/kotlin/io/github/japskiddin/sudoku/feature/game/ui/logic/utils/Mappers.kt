package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.feature.game.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState

internal fun GameState.toUiState(): GameUiState = GameUiState(
    board = this.board,
    selectedCell = this.selectedCell
)
