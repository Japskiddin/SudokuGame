package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.GameError

public sealed class UiState {
    public data object Loading : UiState()

    public data class Error(
        public val code: GameError = GameError.NONE
    ) : UiState()

    public data class Game(
        public val gameState: GameUiState = GameUiState.Initial
    ) : UiState()

    public data object Complete : UiState()

    public companion object {
        public val Initial: UiState = Loading
    }
}
