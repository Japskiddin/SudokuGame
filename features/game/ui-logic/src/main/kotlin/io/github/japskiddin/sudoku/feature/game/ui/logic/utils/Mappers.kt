package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.game.utils.toImmutable
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState

internal fun GameState.toUiState(): GameUiState = GameUiState(
    board = this.board.toImmutable(),
    selectedCell = this.selectedCell,
    type = this.type,
    difficulty = this.difficulty,
    actions = this.actions,
    mistakes = this.mistakes,
    time = this.time
)

internal fun Exception.toGameError(): GameError = when (this) {
    is BoardNotFoundException -> GameError.BOARD_NOT_FOUND
    else -> GameError.UNKNOWN
}
