package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.SavedGame

internal data class GameState(
    val mode: GameMode,
    val lastGame: SavedGame
) {
    internal companion object {
        var Initial = GameState(
            mode = GameMode.Initial,
            lastGame = SavedGame.EMPTY,
        )
    }
}
