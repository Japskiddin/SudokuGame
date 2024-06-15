package io.github.japskiddin.sudoku.feature.game.domain

import io.github.japskiddin.sudoku.core.game.GameError

public sealed class UiState {
    public data object Loading : UiState()

    public data class Error(
        public val code: GameError = GameError.NONE
    ) : UiState()

    public data class Game(
        public val gameState: GameState = GameState.Initial
    ) : UiState()

    public companion object {
        public val Initial: UiState = Loading
    }
}
