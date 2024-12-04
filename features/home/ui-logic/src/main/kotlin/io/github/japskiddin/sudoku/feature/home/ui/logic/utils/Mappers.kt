package io.github.japskiddin.sudoku.feature.home.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState

internal fun mapToUiMenuState(
    mode: GameMode,
    lastGame: SavedGame?
): UiState.Menu = UiState.Menu(
    isShowContinueButton = lastGame != null,
    gameMode = mode,
)
