package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameError

internal data class MenuState(
    val error: GameError,
    val isLoading: Boolean
) {
    internal companion object {
        val Initial = MenuState(
            error = GameError.NONE,
            isLoading = false
        )
    }
}
