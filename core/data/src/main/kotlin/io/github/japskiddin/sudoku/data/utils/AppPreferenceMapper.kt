package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.datastore.model.AppPreferencesDSO

internal fun AppPreferencesDSO.toAppPreferences(): AppPreferences = AppPreferences(
    isMistakesLimit = this.isMistakesLimit,
    isShowTimer = this.isShowTimer,
    isResetTimer = this.isResetTimer,
    isHighlightErrorCells = this.isHighlightErrorCells,
    isHighlightSimilarCells = this.isHighlightSimilarCells,
    isShowRemainingNumbers = this.isShowRemainingNumbers,
    isHighlightSelectedCell = this.isHighlightSelectedCell,
    isKeepScreenOn = this.isKeepScreenOn,
    isSaveGameMode = this.isSaveGameMode
)
