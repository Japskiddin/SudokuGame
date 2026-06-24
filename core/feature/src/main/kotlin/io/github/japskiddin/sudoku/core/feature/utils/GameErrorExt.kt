package io.github.japskiddin.sudoku.core.feature.utils

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

public fun Exception.toGameError(): GameError = when (this) {
    is BoardNotFoundException -> GameError.BOARD_NOT_FOUND
    is SudokuNotGeneratedException -> GameError.SUDOKU_NOT_GENERATED
    else -> GameError.UNKNOWN
}

@StringRes
public fun GameError.toStringRes(): Int = when (this) {
    GameError.BOARD_NOT_FOUND -> CoreUiR.string.err_generate_level
    GameError.SUDOKU_NOT_GENERATED -> CoreUiR.string.err_generate_sudoku
    else -> CoreUiR.string.err_unknown
}
