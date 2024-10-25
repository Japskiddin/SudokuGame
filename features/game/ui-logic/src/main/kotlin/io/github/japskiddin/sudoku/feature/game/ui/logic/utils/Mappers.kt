package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.core.game.utils.toImmutable
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun GameState.toGameUiState(): GameUiState = GameUiState(
    board = this.board.toImmutable(),
    selectedCell = this.selectedCell,
    type = this.type,
    difficulty = this.difficulty,
    actions = this.actions,
    mistakes = this.mistakes,
    time = this.time
)

internal fun Flow<GameState>.asUiState(): Flow<UiState> = map { state ->
    when {
        state.error != GameError.NONE -> UiState.Error(state.error)
        state.status == GameState.Status.LOADING -> UiState.Loading
        state.status == GameState.Status.COMPLETED -> UiState.Complete
        state.status == GameState.Status.FAILED -> UiState.Fail
        else -> UiState.Game(state.toGameUiState())
    }
}
