package io.github.japskiddin.sudoku.feature.home.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.feature.home.ui.logic.utils.toGameError
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@HiltViewModel
public class HomeViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val createBoardUseCase: Provider<CreateBoardUseCase>,
    private val generateSudokuUseCase: Provider<GenerateSudokuUseCase>,
    private val getCurrentYearUseCase: Provider<GetCurrentYearUseCase>,
    getLastGameUseCase: Provider<GetLastGameUseCase>
) : ViewModel() {
    private val difficulties = persistentListOf(
        GameDifficulty.EASY,
        GameDifficulty.INTERMEDIATE,
        GameDifficulty.HARD,
        GameDifficulty.EXPERT
    )
    private val types = persistentListOf(
        GameType.DEFAULT6X6,
        GameType.DEFAULT9X9,
        GameType.DEFAULT12X12
    )

    private val lastGame = getLastGameUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val menuState = MutableStateFlow(MenuState())
    private val gameState = MutableStateFlow(
        GameState(
            selectedDifficulty = GameDifficulty.EASY,
            selectedType = GameType.DEFAULT9X9
        )
    )
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow(GameError.NONE)

    public val uiState: StateFlow<UiState> =
        combine(isLoading, error, menuState, gameState, lastGame) { isLoading, error, menuState, gameState, lastGame ->
            when {
                error != GameError.NONE -> UiState.Error(code = error)
                isLoading -> UiState.Loading
                else -> UiState.Menu(
                    isShowContinueButton = lastGame != null,
                    isShowContinueDialog = menuState.isShowContinueDialog,
                    isShowDifficultyDialog = menuState.isShowDifficultyDialog,
                    selectedDifficulty = gameState.selectedDifficulty,
                    selectedType = gameState.selectedType
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initial)

    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    public fun onStartButtonClick() {
        if (lastGame.value != null) {
            menuState.update { it.copy(isShowContinueDialog = true) }
        } else {
            menuState.update { it.copy(isShowDifficultyDialog = true) }
        }
    }

    public fun onContinueDialogConfirm() {
        menuState.update {
            it.copy(
                isShowContinueDialog = false,
                isShowDifficultyDialog = true
            )
        }
    }

    public fun onDifficultyDialogConfirm() {
        menuState.update { it.copy(isShowDifficultyDialog = false) }
        startNewGame()
    }

    public fun onContinueButtonClick(): Unit = tryNavigateToGame(boardUid = lastGame.value?.uid ?: -1L)

    public fun onSettingsButtonClick() {
        TODO("In Development")
    }

    public fun onRecordsButtonClick() {
        TODO("In Development")
    }

    public fun onSelectPreviousGameDifficulty() {
        gameState.update { state ->
            val index = difficulties.indexOf(state.selectedDifficulty)
            state.copy(
                selectedDifficulty = if (index <= 0) {
                    difficulties.last()
                } else {
                    difficulties[index - 1]
                }
            )
        }
    }

    public fun onSelectNextGameDifficulty() {
        gameState.update { state ->
            val index = difficulties.indexOf(state.selectedDifficulty)
            state.copy(
                selectedDifficulty = if (index >= difficulties.lastIndex) {
                    difficulties.first()
                } else {
                    difficulties[index + 1]
                }
            )
        }
    }

    public fun onSelectPreviousGameType() {
        gameState.update { state ->
            val index = types.indexOf(state.selectedType)
            state.copy(
                selectedType = if (index <= 0) {
                    types.last()
                } else {
                    types[index - 1]
                }
            )
        }
    }

    public fun onSelectNextGameType() {
        gameState.update { state ->
            val index = types.indexOf(state.selectedType)
            state.copy(
                selectedType = if (index >= types.lastIndex) {
                    types.first()
                } else {
                    types[index + 1]
                }
            )
        }
    }

    public fun onCloseError() {
        error.update { GameError.NONE }
    }

    public fun onDismissContinueDialog(): Unit = menuState.update { it.copy(isShowContinueDialog = false) }

    public fun onDismissDifficultyDialog(): Unit = menuState.update { it.copy(isShowDifficultyDialog = false) }

    private fun startNewGame() {
        viewModelScope.launch(appDispatchers.io) {
            isLoading.update { true }

            val board = try {
                generateSudokuUseCase.get().invoke(
                    difficulty = gameState.value.selectedDifficulty,
                    type = gameState.value.selectedType
                )
            } catch (ex: SudokuNotGeneratedException) {
                error.update { ex.toGameError() }
                return@launch
            } finally {
                isLoading.update { false }
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)
        }
    }

    private fun tryNavigateToGame(boardUid: Long) = appNavigator.tryNavigateTo(Destination.GameScreen(boardUid))

    private suspend fun navigateToGame(boardUid: Long) = appNavigator.navigateTo(Destination.GameScreen(boardUid))
}
