package io.github.japskiddin.sudoku.feature.history.ui.logic

public sealed interface UiAction {
    public data object Back : UiAction
}