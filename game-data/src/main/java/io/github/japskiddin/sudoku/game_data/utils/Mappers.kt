package io.github.japskiddin.sudoku.game_data.utils

import io.github.japskiddin.sudoku.datastore.models.GameLevelDO
import io.github.japskiddin.sudoku.game_data.models.Difficulty
import io.github.japskiddin.sudoku.game_data.models.GameLevel

internal fun GameLevelDO.toGameLevel(): GameLevel {
    return GameLevel(
        this.time,
        this.board,
        this.actions,
        this.difficulty.toDifficulty(),
    )
}

internal fun Int.toDifficulty(): Difficulty {
    return when (this) {
        1 -> Difficulty.EASY
        2 -> Difficulty.NORMAL
        3 -> Difficulty.HARD
        4 -> Difficulty.EXPERT
        else -> Difficulty.NORMAL
    }
}

internal fun Difficulty.toInt(): Int {
    return when (this) {
        Difficulty.EASY -> 1
        Difficulty.NORMAL -> 2
        Difficulty.HARD -> 3
        Difficulty.EXPERT -> 4
    }
}