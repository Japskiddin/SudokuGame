package io.github.japskiddin.sudoku.feature.home.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType

public sealed class UiAction {
    public data object PrepareNewGame : UiAction()
    public data object ContinueGame : UiAction()
    public data object ShowSettings : UiAction()
    public data object ShowRecords : UiAction()
    public data object ContinueDialogConfirm : UiAction()
    public data object ContinueDialogDismiss : UiAction()
    public data class DifficultyDialogConfirm(val difficulty: GameDifficulty, val type: GameType) : UiAction()
    public data object DifficultyDialogDismiss : UiAction()
    public data object CloseError : UiAction()
}
