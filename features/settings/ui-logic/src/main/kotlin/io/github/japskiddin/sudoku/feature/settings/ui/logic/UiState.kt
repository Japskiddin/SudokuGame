package io.github.japskiddin.sudoku.feature.settings.ui.logic

public data class UiState(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean,
    val isHighlightErrorCells: Boolean,
    val isHighlightSimilarCells: Boolean,
    val isShowRemainingNumbers: Boolean,
    val isHighlightSelectedCell: Boolean,
    val isKeepScreenOn: Boolean,
    val isSaveGameMode: Boolean,
) {
    public companion object {
        public val Initial: UiState = UiState(
            isMistakesLimit = true,
            isShowTimer = true,
            isResetTimer = false,
            isHighlightErrorCells = true,
            isHighlightSimilarCells = true,
            isHighlightSelectedCell = true,
            isShowRemainingNumbers = true,
            isKeepScreenOn = false,
            isSaveGameMode = true,
        )
    }
}
