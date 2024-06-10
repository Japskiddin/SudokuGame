package io.github.japskiddin.sudoku.feature.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.feature.home.domain.UiState.Loading
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetCurrentYearUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetLastGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGenerated
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getLastGameUseCase: Provider<GetLastGameUseCase>,
    private val getCurrentYearUseCase: Provider<GetCurrentYearUseCase>
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState.Initial)
    public val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()
    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                UiState.Menu()
            }
        }
    }

    public fun onStartClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { Loading(message = R.string.preparing_game_please_wait) }

            val board = try {
                generateSudokuUseCase.get().invoke()
            } catch (ex: SudokuNotGenerated) {
                _uiState.update {
                    UiState.Menu(
                        errorMessage = R.string.err_generate_sudoku
                    )
                }
                return@launch
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)

            _uiState.update {
                UiState.Menu()
            }
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

    private fun hasCurrentGame() =
        getLastGameUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private fun navigateToGame(boardUid: Long) {
        appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
    }
}
