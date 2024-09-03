package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType

public sealed class UiState {
    public data object Loading : UiState()

    public data class Error(
        public val code: GameError = GameError.NONE
    ) : UiState()

    public data class Menu(
        public val isShowContinueButton: Boolean = false,
        public val isShowContinueDialog: Boolean = false,
        public val isShowDifficultyDialog: Boolean = false,
        public val selectedDifficulty: GameDifficulty = GameDifficulty.EASY,
        public val selectedType: GameType = GameType.DEFAULT9X9
    ) : UiState()

    public companion object {
        public val Initial: UiState = Menu()
    }
}
