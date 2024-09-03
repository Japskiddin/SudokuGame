package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType

public data class GameState(
    public val selectedDifficulty: GameDifficulty,
    public val selectedType: GameType
)
