package io.github.japskiddin.sudoku.datastore.model

public data class AppPreferencesDSO(
    val isMistakesLimit: Boolean,
    val isShowTimer: Boolean,
    val isResetTimer: Boolean,
    val isHighlightErrorCells: Boolean,
    val isHighlightSimilarCells: Boolean,
    val isShowRemainingNumbers: Boolean,
    val isHighlightSelectedCell: Boolean,
    val isKeepScreenOn: Boolean,
    val isSaveLastGameMode: Boolean,
)
