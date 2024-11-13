package io.github.japskiddin.sudoku.feature.settings.ui.logic

public data class UiState(
    val isMistakesLimit: Boolean,
    val isTimer: Boolean
) {
    public companion object {
        public val Initial: UiState = UiState(
            isMistakesLimit = true,
            isTimer = true
        )
    }
}
