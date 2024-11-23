package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState

internal fun GameState.toGameUiState(): GameUiState = GameUiState(
    board = this.board.toImmutable(),
    selectedCell = this.selectedCell,
    type = this.type,
    difficulty = this.difficulty,
    actions = this.actions,
    mistakes = this.mistakes,
    time = this.time
)
