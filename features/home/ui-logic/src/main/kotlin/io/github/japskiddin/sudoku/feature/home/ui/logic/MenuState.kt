package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameError

internal data class MenuState(
    val isShowContinueDialog: Boolean,
    val isShowDifficultyDialog: Boolean,
    val error: GameError,
    val isLoading: Boolean
) {
    internal companion object {
        val Initial = MenuState(
            isShowContinueDialog = false,
            isShowDifficultyDialog = false,
            error = GameError.NONE,
            isLoading = false
        )
    }
}
