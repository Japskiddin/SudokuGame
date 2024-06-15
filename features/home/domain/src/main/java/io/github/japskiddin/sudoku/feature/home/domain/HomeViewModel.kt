package io.github.japskiddin.sudoku.feature.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.model.SavedGame
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
        getLastGameUseCase.get().invoke().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val loadingMessage = MutableStateFlow<Int?>(null)
    private val errorMessage = MutableStateFlow<Int?>(null)

    public val uiState: StateFlow<UiState> =
        combine(lastGame, loadingMessage, errorMessage) { lastGame, loadingMessage, errorMessage ->
            if (loadingMessage != null) {
                UiState.Loading(message = loadingMessage)
            } else {
                UiState.Menu(errorMessage = errorMessage, lastGame = lastGame)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Initial)

    public val currentYear: String
        get() = getCurrentYearUseCase.get().invoke()

    public fun onStartClick() {
        viewModelScope.launch(Dispatchers.IO) {
            errorMessage.update { null }
            loadingMessage.update { R.string.preparing_game_please_wait }

            val board = try {
                generateSudokuUseCase.get().invoke()
            } catch (ex: SudokuNotGenerated) {
                errorMessage.update { R.string.err_generate_sudoku }
                loadingMessage.update { null }
                return@launch
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)
            loadingMessage.update { null }
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

    private fun navigateToGame(boardUid: Long) = appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
}
