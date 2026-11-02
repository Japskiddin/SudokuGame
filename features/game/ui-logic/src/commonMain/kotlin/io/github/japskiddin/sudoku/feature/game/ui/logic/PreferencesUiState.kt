package io.github.japskiddin.sudoku.feature.game.ui.logic

public data class PreferencesUiState(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean,
    val isHighlightErrorCells: Boolean,
    val isHighlightSimilarCells: Boolean,
    val isShowRemainingNumbers: Boolean,
    val isHighlightSelectedCell: Boolean,
    val isKeepScreenOn: Boolean,
) {
    public companion object {
        public val Initial: PreferencesUiState = PreferencesUiState(
            isMistakesLimit = true,
            isShowTimer = true,
            isResetTimer = false,
            isHighlightErrorCells = true,
            isHighlightSimilarCells = true,
            isHighlightSelectedCell = true,
            isShowRemainingNumbers = true,
            isKeepScreenOn = false,
        )
    }
}
