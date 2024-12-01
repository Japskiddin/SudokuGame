package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameMode

public sealed interface UiState {
    public data object Loading : UiState

    public data class Error(
        public val code: GameError = GameError.NONE
    ) : UiState

    public data class Menu(
        public val isShowContinueButton: Boolean = false,
        public val selectedGameMode: GameMode = GameMode.Initial,
    ) : UiState

    public companion object {
        public val Initial: UiState = Menu()
    }
}
