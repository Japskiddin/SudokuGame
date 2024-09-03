package io.github.japskiddin.sudoku.feature.home.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGeneratedException

internal fun Exception.toGameError(): GameError = when (this) {
    is SudokuNotGeneratedException -> GameError.SUDOKU_NOT_GENERATED
    else -> GameError.UNKNOWN
}
