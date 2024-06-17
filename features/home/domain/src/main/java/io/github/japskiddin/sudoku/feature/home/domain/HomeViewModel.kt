package io.github.japskiddin.sudoku.feature.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.game.GameError
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.feature.home.domain.utils.toGameError
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class HomeViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val createBoardUseCase: Provider<CreateBoardUseCase>,
    private val generateSudokuUseCase: Provider<GenerateSudokuUseCase>,
    private val getCurrentYearUseCase: Provider<GetCurrentYearUseCase>,
    getLastGameUseCase: Provider<GetLastGameUseCase>
) : ViewModel() {
    private val lastGame = getLastGameUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val menuState = MutableStateFlow(MenuState())
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow(GameError.NONE)

    public val uiState: StateFlow<UiState> =
        combine(isLoading, error, menuState, lastGame) { isLoading, error, menuState, lastGame ->
            when {
                error != GameError.NONE -> UiState.Error(code = error)
                isLoading -> UiState.Loading
                else -> UiState.Menu(
                    isShowContinueButton = lastGame != null,
                    isShowContinueDialog = menuState.isShowContinueDialog,
                    isShowDifficultyDialog = menuState.isShowDifficultyDialog
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initial)

    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    public fun onStartClick() {
        if (lastGame.value != null) {
            menuState.update { it.copy(isShowContinueDialog = true) }
        } else {
            onStartNewGame()
        }
    }

    public fun onContinueGameClick() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.update { true }
            val boardUid = lastGame.value?.uid ?: -1L
            navigateToGame(boardUid)
            isLoading.update { false }
        }
    }

    public fun onSettingsClick() {
        TODO("In Development")
    }

    public fun onRecordsClick() {
        TODO("In Development")
    }

    public fun onDismissContinueDialog() {
        menuState.update { it.copy(isShowContinueDialog = false) }
    }

    public fun onDismissDifficultyDialog() {

    }

    public fun onStartNewGame() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.update { true }

            val board = try {
                generateSudokuUseCase.get().invoke()
            } catch (ex: SudokuNotGeneratedException) {
                isLoading.update { false }
                error.update { ex.toGameError() }
                return@launch
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)
            isLoading.update { false }
        }
    }

    private fun navigateToGame(boardUid: Long) = appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
}
