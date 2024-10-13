package io.github.japskiddin.sudoku.feature.home.ui.logic

public sealed class UiAction {
    public data object PrepareNewGame : UiAction()
    public data object ContinueGame : UiAction()
    public data object ShowSettings : UiAction()
    public data object ShowRecords : UiAction()
    public data object ContinueDialogConfirm : UiAction()
    public data object ContinueDialogDismiss : UiAction()
    public data object DifficultyDialogConfirm : UiAction()
    public data object DifficultyDialogDismiss : UiAction()
    public data object SelectPreviousGameDifficulty : UiAction()
    public data object SelectNextGameDifficulty : UiAction()
    public data object SelectPreviousGameType : UiAction()
    public data object SelectNextGameType : UiAction()
    public data object CloseError : UiAction()
}
