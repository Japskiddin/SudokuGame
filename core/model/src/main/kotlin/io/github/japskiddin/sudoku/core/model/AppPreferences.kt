package io.github.japskiddin.sudoku.core.model

public data class AppPreferences(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean,
    val isHighlightErrorCells: Boolean,
    val isHighlightSimilarCells: Boolean,
    val isShowRemainingNumbers: Boolean,
    val isHighlightSelectedCell: Boolean,
    val isKeepScreenOn: Boolean,
    val isSaveGameMode: Boolean,
)
