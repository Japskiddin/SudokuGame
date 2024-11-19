package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType

public sealed interface UiAction {
    public data object ContinueGame : UiAction
    public data object ShowSettings : UiAction
    public data object ShowRecords : UiAction
    public data class PrepareNewGame(val difficulty: GameDifficulty, val type: GameType) : UiAction
    public data object CloseError : UiAction
}
