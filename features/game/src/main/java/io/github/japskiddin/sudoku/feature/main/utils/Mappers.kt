package io.github.japskiddin.sudoku.feature.main.utils

import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.GameLevelUi

internal fun GameLevel.toState(): GameLevelUi {
    return GameLevelUi(
        time = this.time,
        board = this.board,
        completedBoard = this.completedBoard,
        actions = this.actions,
        difficulty = this.difficulty,
    )
}