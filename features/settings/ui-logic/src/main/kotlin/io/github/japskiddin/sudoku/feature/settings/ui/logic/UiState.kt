package io.github.japskiddin.sudoku.feature.settings.ui.logic

public data class UiState(
    val isEnableMistakesLimit: Boolean
) {
    public companion object {
        public val Initial: UiState = UiState(
            isEnableMistakesLimit = true
        )
    }
}
