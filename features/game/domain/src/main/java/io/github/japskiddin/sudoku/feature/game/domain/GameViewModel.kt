package io.github.japskiddin.sudoku.feature.game.domain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.data.BoardRepository.BoardNotFoundException
import io.github.japskiddin.sudoku.feature.game.domain.UiState.Loading
import io.github.japskiddin.sudoku.feature.game.domain.UiState.Success
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class GameViewModel
@Inject
internal constructor(
    private val getSavedGameUseCase: Provider<GetSavedGameUseCase>,
    private val getBoardUseCase: Provider<GetBoardUseCase>,
    private val appNavigator: AppNavigator,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private var _uiState = MutableStateFlow(UiState.Initial)
    public val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    // val uiState: StateFlow<UiState> = combine(
    //   _isLoading, _gameLevel
    // ) { isLoading, gameLevel ->
    //   if (isLoading) {
    //     UiState.Loading
    //   } else {
    //     if (gameLevel.isEmptyBoard()) {
    //       UiState.Error(R.string.err_generate_level)
    //     } else {
    //       val gameLevelUi = gameLevel.toState()
    //       UiState.Success(gameLevelUi)
    //     }
    //   }
    // }
    //   .stateIn(
    //     scope = viewModelScope,
    //     started = WhileUiSubscribed,
    //     initialValue = UiState.None,
    //   )

    init {
        generateGameLevel()
    }

    public fun onInputCell(
        @Suppress("UNUSED_PARAMETER") cell: Pair<Int, Int>,
        @Suppress("UNUSED_PARAMETER") item: Int
    ) {
//     viewModelScope.launch {
//       val level = _gameLevel.value ?: return@launch
//       val board = level.currentBoard.copyOf()
//       board[cell.first][cell.second] = item
//       _gameLevel.update {
// //                val board = it?.currentBoard ?: return@launch
// //                board[cell.first][cell.second] = item
// //                it
//         it.copy(currentBoard = board)
//       }
//     }
    }

    public fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

    public fun onUpdateSelectedBoardCell(boardCell: BoardCell) {
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            if (state is Success) {
                val gameState = state.gameState
                _uiState.update { Success(gameState = gameState.copy(selectedCell = boardCell)) }
            }
        }
    }

    private fun generateGameLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { Loading(message = R.string.level_creation) }

            val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
            if (boardUid == -1L) {
                _uiState.update { UiState.Error(message = R.string.err_generate_level) }
                return@launch
            }
            val board = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (@Suppress("TooGenericExceptionCaught") ex: Exception) {
                _uiState.update {
                    UiState.Error(
                        message = when (ex) {
                            is BoardNotFoundException -> R.string.err_generate_level
                            else -> R.string.err_unknown
                        }
                    )
                }
                return@launch
            }

            @Suppress("UNUSED_VARIABLE")
            val savedGame = getSavedGameUseCase.get().invoke(board.uid)

            val parser = SudokuParser()
            val list =
                parser.parseBoard(board.initialBoard, board.type)
                    .map { item -> item.toImmutableList() }
                    .toImmutableList()
            _uiState.update { Success(gameState = GameState(board = list)) }
        }
        // _isLoading.value = true
        // viewModelScope.launch {
        //   _gameLevel.update { getBoardUseCaseOld.get().invoke() }
        //   delay(1000)
        //   _isLoading.value = false
        // }
    }
}
