package io.github.japskiddin.sudoku.feature.main.utils

import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.State

internal fun GameLevel.toState(): State {
    return State.Success(this)
}