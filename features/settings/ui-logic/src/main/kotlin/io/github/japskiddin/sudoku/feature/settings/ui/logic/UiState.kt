package io.github.japskiddin.sudoku.feature.settings.ui.logic

public data class UiState(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean
) {
    public companion object {
        public val Initial: UiState = UiState(
            isMistakesLimit = true,
            isShowTimer = true,
            isResetTimer = false
        )
    }
}
