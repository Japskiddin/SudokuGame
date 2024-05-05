package io.github.japskiddin.sudoku.datastore.utils

import io.github.japskiddin.sudoku.datastore.GameLevelDSO
import io.github.japskiddin.sudoku.datastore.models.GameLevelDO

internal fun GameLevelDSO.toGameLevelDO(): GameLevelDO {
    return GameLevelDO(
        this.playtime,
        this.defaultBoard,
        this.currentBoard,
        this.completedBoard,
        this.actions,
        this.difficulty,
    )
}