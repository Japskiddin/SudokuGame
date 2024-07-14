package io.github.japskiddin.sudoku.feature.game.domain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.game.GameError
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
@Suppress("LongParameterList")
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val savedState: SavedStateHandle,
    private val getSavedGameUseCase: Provider<GetSavedGameUseCase>,
    private val getBoardUseCase: Provider<GetBoardUseCase>,
    private val insertSavedGameUseCase: Provider<InsertSavedGameUseCase>,
    private val updateSavedGameUseCase: Provider<UpdateSavedGameUseCase>
) : ViewModel() {
    private lateinit var boardEntity: Board
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow(GameError.NONE)
    private val gameState = MutableStateFlow(GameState.Initial)

    public val uiState: StateFlow<UiState> =
        combine(isLoading, error, gameState) { isLoading, error, gameState ->
            when {
                error != GameError.NONE -> UiState.Error(code = error)
                isLoading -> UiState.Loading
                else -> UiState.Game(gameState = gameState)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initial)

    init {
        generateGameLevel()
    }

    public fun onInputCell(
        num: Int
    ) {
        if (gameState.value.selectedCell == BoardCell.Empty) return
        viewModelScope.launch(appDispatchers.io) {
            gameState.update { state ->
                val list = state.board.map { item -> item.toMutableList() }.toMutableList()
                list[state.selectedCell.row][state.selectedCell.col].value = num

                state.copy(board = list.map { item -> item.toImmutableList() }.toImmutableList())
            }
            saveGame()
        }
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
        viewModelScope.launch(appDispatchers.io) {
            gameState.update { it.copy(selectedCell = boardCell) }
        }
    }

    private fun generateGameLevel() {
        viewModelScope.launch(appDispatchers.io) {
            error.update { GameError.NONE }
            isLoading.update { true }

            val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
            if (boardUid == -1L) {
                error.update { GameError.BOARD_NOT_FOUND }
                isLoading.update { false }
                return@launch
            }

            boardEntity = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (ex: BoardNotFoundException) {
                error.update { ex.toGameError() }
                isLoading.update { false }
                return@launch
            }

            val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)
            if (savedGame != null) {
                val parser = SudokuParser()
                val list = parser.parseBoard(savedGame.currentBoard, boardEntity.type)
                    .map { item -> item.toImmutableList() }
                    .toImmutableList()
                gameState.update { it.copy(board = list) }
            } else {
                val parser = SudokuParser()
                val list = parser.parseBoard(boardEntity.initialBoard, boardEntity.type)
                    .map { item -> item.toImmutableList() }
                    .toImmutableList()
                gameState.update { it.copy(board = list) }
            }

            isLoading.update { false }

            saveGame()
        }
    }

    private fun Exception.toGameError(): GameError = when (this) {
        is BoardNotFoundException -> GameError.BOARD_NOT_FOUND
        else -> GameError.UNKNOWN
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
