package io.github.japskiddin.sudoku.datastore

import io.github.japskiddin.sudoku.datastore.models.GameLevelDO

internal fun GameLevelDSO.toGameLevelDO(): GameLevelDO {
    return GameLevelDO(
        this.time,
        this.board,
        this.actions,
        this.difficulty,
    )
}