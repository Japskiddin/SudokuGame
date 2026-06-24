package io.github.japskiddin.sudoku.feature.settings.ui.logic

public sealed interface UiAction {
    public data class UpdateMistakesLimit(val checked: Boolean) : UiAction

    public data class UpdateShowTimer(val checked: Boolean) : UiAction

    public data class UpdateResetTimer(val checked: Boolean) : UiAction

    public data class UpdateHighlightErrorCells(val checked: Boolean) : UiAction

    public data class UpdateHighlightSimilarCells(val checked: Boolean) : UiAction

    public data class UpdateKeepScreenOn(val checked: Boolean) : UiAction

    public data class UpdateHighlightSelectedCell(val checked: Boolean) : UiAction

    public data class UpdateSaveGameMode(val checked: Boolean) : UiAction

    public data class UpdateShowRemainingNumbers(val checked: Boolean) : UiAction

    public data object Back : UiAction
}
