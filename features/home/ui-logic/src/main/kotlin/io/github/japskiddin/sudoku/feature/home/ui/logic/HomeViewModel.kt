package io.github.japskiddin.sudoku.feature.home.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
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

@Suppress("TooManyFunctions", "LongParameterList")
@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
public class HomeViewModel(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val settingsRepository: SettingsRepository,
    private val boardRepository: BoardRepository,
    savedGameRepository: SavedGameRepository,
    private val generateSudokuUseCase: GenerateSudokuUseCase,
    private val deleteSavedGameUseCase: DeleteSavedGameUseCase,
    getGameModeUseCase: GetGameModePreferenceUseCase,
) : ViewModel() {
    private val gameState: StateFlow<GameState> = combine(
        getGameModeUseCase.invoke(),
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
            deleteSavedGameUseCase.invoke(savedGame)

            val board = try {
                generateSudokuUseCase.invoke(mode)
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

    public companion object {
        public fun factory(
            appNavigator: AppNavigator,
            appDispatchers: AppDispatchers,
            settingsRepository: SettingsRepository,
            boardRepository: BoardRepository,
            savedGameRepository: SavedGameRepository,
            generateSudokuUseCase: GenerateSudokuUseCase,
            deleteSavedGameUseCase: DeleteSavedGameUseCase,
            getGameModeUseCase: GetGameModePreferenceUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    appNavigator = appNavigator,
                    appDispatchers = appDispatchers,
                    settingsRepository = settingsRepository,
                    boardRepository = boardRepository,
                    savedGameRepository = savedGameRepository,
                    generateSudokuUseCase = generateSudokuUseCase,
                    deleteSavedGameUseCase = deleteSavedGameUseCase,
                    getGameModeUseCase = getGameModeUseCase,
                )
            }
        }
    }
}
