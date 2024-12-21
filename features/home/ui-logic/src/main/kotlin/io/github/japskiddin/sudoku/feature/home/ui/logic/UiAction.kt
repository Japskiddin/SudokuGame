package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameMode

public sealed interface UiAction {
    public data object ContinueGame : UiAction
    public data object ShowSettings : UiAction
    public data object ShowHistory : UiAction
    public data class PrepareNewGame(val mode: GameMode) : UiAction
    public data object CloseError : UiAction
}
