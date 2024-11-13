package io.github.japskiddin.sudoku.feature.settings.ui.logic

public sealed interface UiAction {
    public data class UpdateMistakesLimit(val checked: Boolean) : UiAction
    public data class UpdateTimer(val checked: Boolean) : UiAction
}
