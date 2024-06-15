package io.github.japskiddin.sudoku.feature.game.domain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.qqwing.GameStatus
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.data.BoardRepository.BoardNotFoundException
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.InsertSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.UpdateSavedGameUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.collections.immutable.toImmutableList
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
public class GameViewModel
@Inject
internal constructor(
    private val getSavedGameUseCase: Provider<GetSavedGameUseCase>,
    private val getBoardUseCase: Provider<GetBoardUseCase>,
    private val insertSavedGameUseCase: Provider<InsertSavedGameUseCase>,
    private val updateSavedGameUseCase: Provider<UpdateSavedGameUseCase>,
    private val appNavigator: AppNavigator,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private lateinit var boardEntity: Board
    private val loadingMessage = MutableStateFlow<Int?>(null)
    private val errorMessage = MutableStateFlow<Int?>(null)
    private val gameState = MutableStateFlow(GameState.Initial)

    public val uiState: StateFlow<UiState> =
        combine(loadingMessage, errorMessage, gameState) { loadingMessage, errorMessage, gameState ->
            if (errorMessage != null) {
                UiState.Error(message = errorMessage)
            } else if (loadingMessage != null) {
                UiState.Loading(message = loadingMessage)
            } else {
                UiState.Game(gameState = gameState)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Initial)

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

    public fun onBackButtonClicked(): Unit = appNavigator.tryNavigateBack()

    public fun onUpdateSelectedBoardCell(boardCell: BoardCell) {
        viewModelScope.launch(Dispatchers.IO) {
            gameState.update { it.copy(selectedCell = boardCell) }
        }
    }

    private fun generateGameLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            errorMessage.update { null }
            loadingMessage.update { R.string.level_creation }

            val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
            if (boardUid == -1L) {
                errorMessage.update { R.string.err_generate_level }
                loadingMessage.update { null }
                return@launch
            }
            boardEntity = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (@Suppress("TooGenericExceptionCaught") ex: Exception) {
                errorMessage.update {
                    when (ex) {
                        is BoardNotFoundException -> R.string.err_generate_level
                        else -> R.string.err_unknown
                    }
                }
                loadingMessage.update { null }
                return@launch
            }

            @Suppress("UNUSED_VARIABLE")
            val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)

            val parser = SudokuParser()
            val list = parser.parseBoard(boardEntity.initialBoard, boardEntity.type)
                .map { item -> item.toImmutableList() }
                .toImmutableList()

            gameState.update { it.copy(board = list) }
            loadingMessage.update { null }

            saveGame()
        }
    }

    private suspend fun saveGame() {
        val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)
        val sudokuParser = SudokuParser()
        if (savedGame != null) {
            updateSavedGameUseCase.get().invoke(
                savedGame = savedGame,
                timer = 0L,
                currentBoard = sudokuParser.boardToString(gameState.value.board),
                notes = sudokuParser.notesToString(gameState.value.notes),
                mistakes = 0,
                lastPlayed = 0
            )
        } else {
            insertSavedGameUseCase.get().invoke(
                uid = boardEntity.uid,
                currentBoard = sudokuParser.boardToString(gameState.value.board),
                notes = sudokuParser.notesToString(gameState.value.notes),
                timer = 0L,
                actions = 0,
                mistakes = 0,
                lastPlayed = 0L,
                startedAt = 0L,
                finishedAt = 0L,
                status = GameStatus.IN_PROGRESS,
            )
        }
    }
}
