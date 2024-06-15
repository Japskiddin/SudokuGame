package io.github.japskiddin.sudoku.feature.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.model.SavedGame
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGeneratedException
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
    private val lastGame: StateFlow<SavedGame?> =
        getLastGameUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow(ErrorCode.NONE)

    public val uiState: StateFlow<UiState> =
        combine(isLoading, error, lastGame) { isLoading, error, lastGame ->
            when {
                error != ErrorCode.NONE -> UiState.Error(code = error)
                isLoading -> UiState.Loading
                else -> UiState.Menu(isContinueVisible = lastGame != null)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Initial)

    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    public fun onStartClick() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.update { true }

            val board = try {
                generateSudokuUseCase.get().invoke()
            } catch (ex: SudokuNotGeneratedException) {
                isLoading.update { false }
                sendError(ex)
                return@launch
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)
            isLoading.update { false }
        }
    }

    public fun onContinueGameClick() {
        TODO("In Development")
    }

    public fun onSettingsClick() {
        TODO("In Development")
    }

    public fun onRecordsClick() {
        TODO("In Development")
    }

    private fun sendError(ex: Exception) {
        error.update {
            when (ex) {
                is SudokuNotGeneratedException -> ErrorCode.SUDOKU_NOT_GENERATED
                else -> ErrorCode.UNKNOWN
            }
        }
    }

    private fun navigateToGame(boardUid: Long) = appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
}
