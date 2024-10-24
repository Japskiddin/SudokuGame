package io.github.japskiddin.sudoku.feature.home.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.feature.home.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.home.ui.logic.MenuState
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState

internal fun mapToUiMenuState(
    menuState: MenuState,
    gameState: GameState,
    lastGame: SavedGame?
): UiState.Menu = UiState.Menu(
    isShowContinueButton = lastGame != null,
    isShowContinueDialog = menuState.isShowContinueDialog,
    isShowDifficultyDialog = menuState.isShowDifficultyDialog,
    selectedDifficulty = gameState.selectedDifficulty,
    selectedType = gameState.selectedType
)
