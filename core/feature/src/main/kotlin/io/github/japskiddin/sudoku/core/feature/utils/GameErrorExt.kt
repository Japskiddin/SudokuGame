package io.github.japskiddin.sudoku.core.feature.utils

import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.model.GameError

public fun Exception.toGameError(): GameError = when (this) {
    is BoardNotFoundException -> GameError.BOARD_NOT_FOUND
    is SudokuNotGeneratedException -> GameError.SUDOKU_NOT_GENERATED
    else -> GameError.UNKNOWN
}
