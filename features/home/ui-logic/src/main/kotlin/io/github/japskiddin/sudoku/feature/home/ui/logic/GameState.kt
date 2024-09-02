package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.game.GameDifficulty
import io.github.japskiddin.sudoku.core.game.GameType

public data class GameState(
    public val selectedDifficulty: GameDifficulty,
    public val selectedType: GameType
)
