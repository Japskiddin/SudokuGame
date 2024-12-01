package io.github.japskiddin.sudoku.feature.home.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.feature.home.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState

internal fun mapToUiMenuState(
    gameState: GameState,
    lastGame: SavedGame?
): UiState.Menu = UiState.Menu(
    isShowContinueButton = lastGame != null,
    selectedGameMode = GameMode(
        difficulty = gameState.selectedDifficulty,
        type = gameState.selectedType
    ),
)
