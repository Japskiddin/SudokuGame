package io.github.japskiddin.sudoku.feature.home.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.feature.utils.toGameError
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.feature.home.domain.usecase.DeleteSavedGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetGameModePreferenceUseCase
import io.github.japskiddin.sudoku.feature.home.ui.logic.utils.toMenuUiState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
public class HomeViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val settingsRepository: SettingsRepository,
    private val boardRepository: BoardRepository,
    savedGameRepository: SavedGameRepository,
    private val generateSudokuUseCase: Provider<GenerateSudokuUseCase>,
    private val deleteSavedGameUseCase: Provider<DeleteSavedGameUseCase>,
    getGameModeUseCase: Provider<GetGameModePreferenceUseCase>,
) : ViewModel() {
    private val gameState: StateFlow<GameState> = combine(
        getGameModeUseCase.get().invoke(),
        savedGameRepository.getLast(),
    ) { gameMode, lastGame ->
        GameState(
            mode = gameMode,
            lastGame = lastGame
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = GameState.Initial
    )
    private val menuState = MutableStateFlow(MenuState.Initial)

    public val uiState: StateFlow<UiState> = combine(
        menuState,
        gameState,
    ) { menuState, gameState ->
        when {
            menuState.error != GameError.NONE -> UiState.Error(code = menuState.error)
            menuState.isLoading -> UiState.Loading
            else -> gameState.toMenuUiState()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState.Initial
    )

    public val currentYear: String
        get() = Calendar.getInstance().get(Calendar.YEAR).toString()

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.ContinueGame -> continueCurrentGame()
            is UiAction.ShowSettings -> showSettings()
            is UiAction.ShowHistory -> showHistory()
            is UiAction.PrepareNewGame -> prepareNewGame(action.mode)
            is UiAction.CloseError -> closeError()
        }
    }

    private fun prepareNewGame(mode: GameMode) {
        saveGameMode(mode)
        startNewGame(mode)
    }

    private fun continueCurrentGame() {
        val boardUid = gameState.value.lastGame.uid
        appNavigator.tryNavigateTo(Destination.Game(boardUid))
    }

    private fun showSettings() {
        appNavigator.tryNavigateTo(Destination.Settings())
    }

    private fun showHistory() {
        appNavigator.tryNavigateTo(Destination.History())
    }

    private fun closeError() {
        menuState.update { it.copy(error = GameError.NONE) }
    }

    private fun saveGameMode(mode: GameMode) {
        viewModelScope.launch {
            settingsRepository.setGameMode(mode)
        }
    }

    private fun startNewGame(mode: GameMode) {
        menuState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val savedGame = gameState.value.lastGame
            deleteSavedGameUseCase.get().invoke(savedGame)

            val board = try {
                generateSudokuUseCase.get().invoke(mode)
            } catch (ex: SudokuNotGeneratedException) {
                menuState.update { it.copy(error = ex.toGameError()) }
                return@launch
            } finally {
                menuState.update { it.copy(isLoading = false) }
            }

            val insertedBoardUid = boardRepository.insert(board)
            appNavigator.navigateTo(Destination.Game(insertedBoardUid))
        }
    }
}
