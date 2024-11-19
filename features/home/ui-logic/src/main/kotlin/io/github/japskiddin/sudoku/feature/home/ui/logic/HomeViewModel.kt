package io.github.japskiddin.sudoku.feature.home.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.feature.utils.toGameError
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.ui.logic.utils.mapToUiMenuState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
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
    private val lastGame = getLastGameUseCase.get().invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    private val menuState = MutableStateFlow(MenuState.Initial)
    private val gameState = MutableStateFlow(GameState.Initial)

    public val uiState: StateFlow<UiState> =
        combine(menuState, gameState, lastGame) { menuState, gameState, lastGame ->
            when {
                menuState.error != GameError.NONE -> UiState.Error(code = menuState.error)
                menuState.isLoading -> UiState.Loading
                else -> mapToUiMenuState(gameState, lastGame)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Initial
        )

    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    init {
        // TODO: Загружать из настроек
        gameState.update {
            it.copy(
                selectedDifficulty = GameDifficulty.EASY,
                selectedType = GameType.DEFAULT9X9
            )
        }
    }

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.ContinueGame -> continueCurrentGame()
            is UiAction.ShowSettings -> showSettings()
            is UiAction.ShowRecords -> showRecords()
            is UiAction.PrepareNewGame -> confirmDifficultyDialog(action.difficulty, action.type)
            is UiAction.CloseError -> closeError()
        }
    }

    private fun confirmDifficultyDialog(difficulty: GameDifficulty, type: GameType) {
        gameState.update {
            it.copy(
                selectedDifficulty = difficulty,
                selectedType = type
            )
        }
        startNewGame()
    }

    private fun continueCurrentGame() {
        tryNavigateToGame(boardUid = lastGame.value?.uid ?: -1L)
    }

    private fun showSettings() {
        tryNavigateToSettings()
    }

    private fun showRecords() {
        TODO("In Development")
    }

    private fun closeError() {
        menuState.update { it.copy(error = GameError.NONE) }
    }

    private fun startNewGame() {
        menuState.update { it.copy(isLoading = true) }

        viewModelScope.launch(appDispatchers.io) {
            val board = try {
                generateSudokuUseCase.get().invoke(
                    difficulty = gameState.value.selectedDifficulty,
                    type = gameState.value.selectedType
                )
            } catch (ex: SudokuNotGeneratedException) {
                menuState.update { it.copy(error = ex.toGameError()) }
                return@launch
            } finally {
                menuState.update { it.copy(isLoading = false) }
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)
        }
    }

    private fun tryNavigateToSettings() = appNavigator.tryNavigateTo(Destination.SettingsScreen())

    private fun tryNavigateToGame(boardUid: Long) = appNavigator.tryNavigateTo(Destination.GameScreen(boardUid))

    private suspend fun navigateToGame(boardUid: Long) = appNavigator.navigateTo(Destination.GameScreen(boardUid))
}
