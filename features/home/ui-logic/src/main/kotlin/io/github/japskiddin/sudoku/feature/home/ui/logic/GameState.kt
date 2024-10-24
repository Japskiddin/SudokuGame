package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType

internal data class GameState(
    val selectedDifficulty: GameDifficulty,
    val selectedType: GameType
) {
    internal companion object {
        var Initial = GameState(
            selectedDifficulty = GameDifficulty.EASY,
            selectedType = GameType.DEFAULT9X9
        )
    }
}
