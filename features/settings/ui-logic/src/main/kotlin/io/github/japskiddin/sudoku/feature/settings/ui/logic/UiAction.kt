package io.github.japskiddin.sudoku.feature.settings.ui.logic

public sealed interface UiAction {
    public data class UpdateMistakesLimit(val checked: Boolean) : UiAction

    public data class UpdateShowTimer(val checked: Boolean) : UiAction

    public data class UpdateResetTimer(val checked: Boolean) : UiAction
}
