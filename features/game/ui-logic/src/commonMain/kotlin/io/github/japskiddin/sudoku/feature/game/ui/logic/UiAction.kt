package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell

public sealed interface UiAction {
    public data class InputCell(val value: Int) : UiAction
    public data class SelectBoardCell(val cell: BoardCell) : UiAction
    public data object EraseBoardCell : UiAction
    public data object ResetBoard : UiAction
    public data object Undo : UiAction
    public data object Redo : UiAction
    public data object Note : UiAction
    public data object ScreenResumed : UiAction
    public data object ScreenPaused : UiAction
    public data object ShowMenu : UiAction
    public data object DismissMenu : UiAction
    public data object ShowSettings : UiAction
    public data object Exit : UiAction
}
