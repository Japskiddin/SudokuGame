package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.datastore.model.GameModeDSO

internal fun GameModeDSO.toGameMode(): GameMode = GameMode(
    difficulty = this.difficulty.toGameDifficulty(),
    type = this.type.toGameType(),
)

internal fun GameMode.toGameModeDSO(): GameModeDSO = GameModeDSO(
    difficulty = this.difficulty.toInt(),
    type = this.type.toInt(),
)
