package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.GameError

public sealed interface UiState {
    public data object Loading : UiState

    public data class Error(
        public val code: GameError = GameError.NONE
    ) : UiState

    public data class Game(
        public val gameState: GameUiState = GameUiState.Initial,
        public val preferencesState: PreferencesUiState = PreferencesUiState.Initial
    ) : UiState

    public data object Complete : UiState

    public data object Fail : UiState

    public companion object {
        public val Initial: UiState = Loading
    }
}
