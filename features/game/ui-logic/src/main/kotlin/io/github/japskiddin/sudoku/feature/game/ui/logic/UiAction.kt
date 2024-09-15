package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell

public sealed class UiAction {
    public data class InputCell(val value: Int) : UiAction()
    public data class SelectBoardCell(val cell: BoardCell) : UiAction()
}
