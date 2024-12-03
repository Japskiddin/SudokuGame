package io.github.japskiddin.sudoku.feature.home.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.feature.utils.toGameError
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetGameModePreferenceUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetSaveGameModePreferenceUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SetGameModePreferenceUseCase
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

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
public class HomeViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val createBoardUseCase: Provider<CreateBoardUseCase>,
    private val generateSudokuUseCase: Provider<GenerateSudokuUseCase>,
    private val getCurrentYearUseCase: Provider<GetCurrentYearUseCase>,
    private val setGameModeUseCase: Provider<SetGameModePreferenceUseCase>,
    getSaveGameModeUseCase: Provider<GetSaveGameModePreferenceUseCase>,
    getLastGameUseCase: Provider<GetLastGameUseCase>,
    getGameModeUseCase: Provider<GetGameModePreferenceUseCase>,
) : ViewModel() {
    private val lastGame = getLastGameUseCase.get().invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    private val isSaveGameMode = getSaveGameModeUseCase.get().invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )
    private val menuState = MutableStateFlow(MenuState.Initial)
    private val gameState: StateFlow<GameState> = combine(
        getGameModeUseCase.get().invoke(),
        isSaveGameMode
    ) { gameMode, isSaveGameMode ->
        if (isSaveGameMode) {
            GameState(mode = gameMode)
        } else {
            GameState.Initial
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = GameState.Initial
    )

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

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.ContinueGame -> continueCurrentGame()
            is UiAction.ShowSettings -> showSettings()
            is UiAction.ShowRecords -> showRecords()
            is UiAction.PrepareNewGame -> prepareNewGame(action.mode)
            is UiAction.CloseError -> closeError()
        }
    }

    private fun prepareNewGame(mode: GameMode) {
        saveCurrentGameMode(mode)
        startNewGame(mode)
    }

    private fun continueCurrentGame() {
        tryNavigateToGame(boardUid = lastGame.value?.uid ?: -1L)
    }

    private fun showSettings() {
        tryNavigateToSettings()
    }

    private fun showRecords() {
    }

    private fun closeError() {
        menuState.update { it.copy(error = GameError.NONE) }
    }

    private fun saveCurrentGameMode(mode: GameMode) {
        val shouldSaveMode = isSaveGameMode.value
        if (shouldSaveMode) {
            viewModelScope.launch {
                setGameModeUseCase.get().invoke(mode)
            }
        }
    }

    private fun startNewGame(mode: GameMode) {
        menuState.update { it.copy(isLoading = true) }

        viewModelScope.launch(appDispatchers.io) {
            val board = try {
                generateSudokuUseCase.get().invoke(mode)
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
