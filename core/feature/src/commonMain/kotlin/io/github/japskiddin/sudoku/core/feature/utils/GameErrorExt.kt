package io.github.japskiddin.sudoku.core.feature.utils

import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.ui.resources.err_generate_level
import io.github.japskiddin.sudoku.core.ui.resources.err_generate_sudoku
import io.github.japskiddin.sudoku.core.ui.resources.err_unknown
import org.jetbrains.compose.resources.StringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

public fun Exception.toGameError(): GameError = when (this) {
    is BoardNotFoundException -> GameError.BOARD_NOT_FOUND
    is SudokuNotGeneratedException -> GameError.SUDOKU_NOT_GENERATED
    else -> GameError.UNKNOWN
}

public fun GameError.toStringRes(): StringResource = when (this) {
    GameError.BOARD_NOT_FOUND -> CoreUiRes.string.err_generate_level
    GameError.SUDOKU_NOT_GENERATED -> CoreUiRes.string.err_generate_sudoku
    else -> CoreUiRes.string.err_unknown
}
