package io.github.japskiddin.sudoku.feature.game.ui.logic

public data class PreferencesUiState(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean,
) {
    public companion object {
        public val Initial: PreferencesUiState = PreferencesUiState(
            isMistakesLimit = true,
            isShowTimer = true,
            isResetTimer = false,
        )
    }
}
