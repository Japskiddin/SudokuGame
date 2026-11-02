package io.github.japskiddin.sudoku.feature.home.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.isCurrent
import io.github.japskiddin.sudoku.feature.home.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState

internal fun GameState.toMenuUiState(): UiState.Menu = UiState.Menu(
    isShowContinueButton = lastGame.isCurrent(),
    gameMode = mode,
)
