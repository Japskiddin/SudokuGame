package io.github.japskiddin.sudoku.feature.settings.ui.logic

public sealed class UiAction {
    public data class UpdateMistakesLimit(val checked: Boolean) : UiAction()
}
