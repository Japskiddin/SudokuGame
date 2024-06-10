package io.github.japskiddin.sudoku.feature.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.feature.home.domain.UiState.Loading
import io.github.japskiddin.sudoku.feature.home.domain.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.SudokuNotGenerated
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class HomeViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val createBoardUseCase: Provider<CreateBoardUseCase>,
    private val generateSudokuUseCase: Provider<GenerateSudokuUseCase>
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState.Initial)
    public val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    public val currentYear: String
        get() = Calendar.getInstance().get(Calendar.YEAR).toString()

    public fun onStartClick() {
        generateNewBoard()
    }

    public fun onSettingsClick() {
        TODO("In Development")
    }

    public fun onRecordsClick() {
        TODO("In Development")
    }

    private fun generateNewBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { Loading(message = R.string.preparing_game_please_wait) }

            val board = try {
                generateSudokuUseCase.get().invoke()
            } catch (ex: SudokuNotGenerated) {
                // TODO сообщить об ошибке
                return@launch
            }

            val insertedBoardUid = createBoardUseCase.get().invoke(board)
            navigateToGame(insertedBoardUid)

            _uiState.update { UiState.Menu }
        }
    }

    private fun navigateToGame(boardUid: Long) {
        appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
    }
}
