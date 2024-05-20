package io.github.japskiddin.sudoku.feature.game

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.model.Difficulty
import io.github.japskiddin.sudoku.data.model.GameLevel
import io.github.japskiddin.sudoku.feature.game.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.utils.WhileUiSubscribed
import io.github.japskiddin.sudoku.feature.game.utils.toState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Dispatchers
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
  private val getSavedGameUseCase: Provider<GetSavedGameUseCase>,
  private val getBoardUseCase: Provider<GetBoardUseCase>,
  private val appNavigator: AppNavigator,
  private val savedState: SavedStateHandle,
) : ViewModel() {
  private val _isLoading = MutableStateFlow(false)
  private val _gameLevel = MutableStateFlow(GameLevel())
  private val _playtime = MutableStateFlow(0L)
  private val _actions = MutableStateFlow(0)

  val uiState: StateFlow<UiState> = combine(
    _isLoading, _gameLevel
  ) { isLoading, gameLevel ->
    if (isLoading) {
      UiState.Loading
    } else {
      if (gameLevel.isEmptyBoard()) {
        UiState.Error(R.string.err_generate_level)
      } else {
        val gameLevelUi = gameLevel.toState()
        UiState.Success(gameLevelUi)
      }
    }
  }
    .stateIn(
      scope = viewModelScope,
      started = WhileUiSubscribed,
      initialValue = UiState.None,
    )

  init {
    generateGameLevel()
  }

  fun onInputCell(cell: Pair<Int, Int>, item: Int) {
    viewModelScope.launch {
      val level = _gameLevel.value ?: return@launch
      val board = level.currentBoard.copyOf()
      board[cell.first][cell.second] = item
      _gameLevel.update {
//                val board = it?.currentBoard ?: return@launch
//                board[cell.first][cell.second] = item
//                it
        it.copy(currentBoard = board)
      }
    }
  }

  fun onBackButtonClicked() {
    appNavigator.tryNavigateBack()
  }

  private fun generateGameLevel() {
    viewModelScope.launch(Dispatchers.IO) {
      // TODO добавить эксепшн BoardNotFoundException
      val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
      if (boardUid == -1L) return@launch
      val board = getBoardUseCase.get().invoke(boardUid) ?: return@launch
      val savedGame = getSavedGameUseCase.get().invoke(board.uid)
    }
    // _isLoading.value = true
    // viewModelScope.launch {
    //   _gameLevel.update { getBoardUseCaseOld.get().invoke() }
    //   delay(1000)
    //   _isLoading.value = false
    // }
  }
}

internal class GameUiState {
  val errorMessage: String? = null
  val isLoading: Boolean = false
  val gameLevel: GameLevelUi? = null
  // val defaultBoard: Array<IntArray> = emptyArray()
  // val currentBoard: Array<IntArray> = emptyArray()
  // val completedBoard: Array<IntArray> = emptyArray()
  // val difficulty: Difficulty = Difficulty.NORMAL
}

internal sealed class UiState {
  data object None : UiState()
  data object Loading : UiState()
  class Error(@StringRes val message: Int) : UiState()
  class Success(val gameLevelUi: GameLevelUi) : UiState()
}

internal class GameLevelUi(
  val defaultBoard: Array<IntArray>,
  val currentBoard: Array<IntArray>,
  val completedBoard: Array<IntArray>,
  val difficulty: Difficulty,
)