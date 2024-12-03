package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameMode

internal data class GameState(
    val mode: GameMode,
) {
    internal companion object {
        var Initial = GameState(
            mode = GameMode.Initial,
        )
    }
}
