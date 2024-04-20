package io.github.japskiddin.sudoku.feature.main

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.models.Difficulty
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.usecase.GenerateGameLevelUseCase
import io.github.japskiddin.sudoku.feature.main.utils.WhileUiSubscribed
import io.github.japskiddin.sudoku.feature.main.utils.toState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val generateGameLevelUseCase: Provider<GenerateGameLevelUseCase>,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _gameLevel = MutableStateFlow<GameLevel?>(null)
    private val _selectedCell = MutableStateFlow(Pair(-1, -1))

    val uiState: StateFlow<UiState> = combine(
        _isLoading, _selectedCell, _gameLevel
    ) { isLoading, selectedCell, gameLevel ->
        if (isLoading) {
            UiState.Loading
        } else {
            if (gameLevel == null) {
                UiState.Error(R.string.err_generate_level)
            } else {
                if (gameLevel.isEmptyBoard()) {
                    UiState.Error(R.string.err_generate_level)
                } else {
                    val gameLevelUi = gameLevel.toState()
                    gameLevelUi.selectedCell = selectedCell
                    UiState.Success(gameLevelUi)
                }
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = UiState.None,
        )

    init {
        onGenerateGameLevel()
    }

    fun updateSelectedCell(i: Int, j: Int) {
        _selectedCell.value = Pair(i, j)
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

    private fun onGenerateGameLevel() {
        _isLoading.value = true
        viewModelScope.launch {
            _gameLevel.update { generateGameLevelUseCase.get().invoke() }
            delay(1000)
            _isLoading.value = false
        }
    }
}

internal sealed class UiState {
    data object None : UiState()
    data object Loading : UiState()
    class Error(@StringRes val message: Int) : UiState()
    class Success(val gameLevelUi: GameLevelUi) : UiState()
}

internal class GameLevelUi(
    val time: Long = 0,
    val board: Array<IntArray> = emptyArray(),
    val completedBoard: Array<IntArray> = emptyArray(),
    val actions: Int = 0,
    val difficulty: Difficulty = Difficulty.NORMAL,
    var selectedCell: Pair<Int, Int> = Pair(-1, -1)
)