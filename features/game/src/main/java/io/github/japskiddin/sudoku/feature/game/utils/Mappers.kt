package io.github.japskiddin.sudoku.feature.game.utils

import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.game.GameLevelUi

internal fun GameLevel.toState(): GameLevelUi {
    return GameLevelUi(
        currentBoard = this.currentBoard,
        completedBoard = this.completedBoard,
        difficulty = this.difficulty,
    )
}