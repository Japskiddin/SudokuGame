package io.github.japskiddin.sudoku.feature.game.ui.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.game.utils.convertToList
import io.github.japskiddin.sudoku.core.game.utils.convertToString
import io.github.japskiddin.sudoku.core.game.utils.initiate
import io.github.japskiddin.sudoku.core.game.utils.isSudokuFilled
import io.github.japskiddin.sudoku.core.game.utils.isValidCell
import io.github.japskiddin.sudoku.core.game.utils.isValidCellDynamic
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.MistakesMethod
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.core.model.isEmpty
import io.github.japskiddin.sudoku.feature.game.domain.usecase.CheckGameCompletedUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.InsertSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.RestoreGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.SolveBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.UpdateSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.copyBoard
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.toGameError
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.toUiState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
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
    private val updateSavedGameUseCase: Provider<UpdateSavedGameUseCase>,
    private val restoreGameUseCase: Provider<RestoreGameUseCase>,
    private val solveBoardUseCase: Provider<SolveBoardUseCase>,
    private val checkGameCompletedUseCase: Provider<CheckGameCompletedUseCase>
) : ViewModel() {
    private lateinit var boardEntity: Board

    private lateinit var gameHistoryManager: GameHistoryManager

    private val isLoading = MutableStateFlow(false)
    private val isCompleted = MutableStateFlow(false)
    private val error = MutableStateFlow(GameError.NONE)
    private val gameState = MutableStateFlow(GameState.Initial)

    private var timerJob: Job? = null

    public val uiState: StateFlow<UiState> = combine(
        isLoading,
        isCompleted,
        error,
        gameState
    ) { isLoading, isCompleted, error, gameState ->
        when {
            isLoading -> UiState.Loading
            isCompleted -> UiState.Complete
            error != GameError.NONE -> UiState.Error(error)
            else -> UiState.Game(gameState.toUiState())
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initial)

    init {
        generateGameLevel()
    }

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.InputCell -> inputValueToCell(action.value)
            is UiAction.SelectBoardCell -> selectBoardCell(action.cell)
            is UiAction.EraseBoardCell -> inputValueToCell(0)
            is UiAction.ResetBoard -> resetBoard()
            is UiAction.Undo -> undoBoard()
            is UiAction.Redo -> redoBoard()
            is UiAction.Note -> notesBoard()
            is UiAction.CloseError -> onBackButtonClick()
        }
    }

    public fun onBackButtonClick(): Unit = appNavigator.tryNavigateBack()

    private fun generateGameLevel() {
        isLoading.update { true }

        val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
        if (boardUid == -1L) {
            error.update { GameError.BOARD_NOT_FOUND }
            isLoading.update { false }
            return
        }

        viewModelScope.launch(appDispatchers.io) {
            boardEntity = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (ex: BoardNotFoundException) {
                error.update { ex.toGameError() }
                isLoading.update { false }
                return@launch
            }

            val initialBoard = boardEntity.board.convertToList(boardEntity.type)
            initialBoard.initiate()
            gameState.update { it.copy(initialBoard = initialBoard) }

            val solvedBoard = if (boardEntity.solvedBoard.isSudokuFilled()) {
                boardEntity.solvedBoard.convertToList(boardEntity.type)
            } else {
                solveBoardUseCase.get().invoke(boardEntity.board, boardEntity.type, initialBoard)
            }
            gameState.update { it.copy(solvedBoard = solvedBoard) }

            val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)
            if (savedGame != null) {
                restoreGame(savedGame)
            } else {
                saveGame()
                gameState.update { it.copy(board = it.initialBoard) }
            }
            isLoading.update { false }
            gameHistoryManager = GameHistoryManager(GameHistory(board = gameState.value.board, notes = listOf()))
//            startTimer()
        }
    }

    private fun restoreGame(savedGame: SavedGame) {
        val savedBoard = savedGame.board.convertToList(boardEntity.type)
        val state = gameState.value
        val restoredBoard = restoreGameUseCase.get().invoke(
            savedBoard,
            boardEntity.type,
            state.initialBoard,
            state.solvedBoard
        )
        gameState.update { it.copy(board = restoredBoard) }
    }

    private fun inputValueToCell(value: Int, mistakesMethod: MistakesMethod = MistakesMethod.CLASSIC) {
        if (gameState.value.selectedCell.isEmpty()) return

        gameState.update { state ->
            val selectedCell = state.selectedCell.copy()
            val newBoard = copyBoard(state.board)
            val cell = newBoard[selectedCell.row][selectedCell.col]
            if (cell.isLocked) return
            cell.value = value
            selectedCell.value = value

            if (value == 0) {
                cell.isError = false
                selectedCell.isError = false
            } else {
                when (mistakesMethod) {
                    MistakesMethod.MODERN -> {
                        newBoard[cell.row][cell.col].isError =
                            !isValidCellDynamic(newBoard, newBoard[cell.row][cell.col], boardEntity.type)
                        newBoard.forEach { cells ->
                            cells.forEach { cell ->
                                if (cell.value != 0 && cell.isError) {
                                    cell.isError = !isValidCellDynamic(newBoard, cell, boardEntity.type)
                                }
                            }
                        }
                    }

                    MistakesMethod.CLASSIC -> cell.isError = isValidCell(newBoard, gameState.value.solvedBoard, cell)
                }

                selectedCell.isError = cell.isError
            }

            state.copy(board = newBoard, selectedCell = selectedCell)
        }
        addToGameHistory()
        checkGameCompleted()
        viewModelScope.launch(appDispatchers.io) {
            saveGame()
        }
    }

    private fun resetBoard() {
        gameState.update { it.copy(board = it.initialBoard) }
        addToGameHistory()
        viewModelScope.launch(appDispatchers.io) {
            saveGame()
        }
    }

    private fun undoBoard() {
        val gameHistory = gameHistoryManager.undo()
        updateBoardFromHistory(gameHistory)
    }

    private fun notesBoard() {
        TODO("Not Implemented")
    }

    private fun selectBoardCell(cell: BoardCell) {
        gameState.update { it.copy(selectedCell = cell) }
    }

    private fun redoBoard() {
        gameHistoryManager.redo()?.let { gameHistory -> updateBoardFromHistory(gameHistory) }
    }

    private fun updateBoardFromHistory(gameHistory: GameHistory) {
        gameState.update { it.copy(board = gameHistory.board, notes = gameHistory.notes) }
        viewModelScope.launch(appDispatchers.io) {
            saveGame()
        }
    }

    private fun addToGameHistory() {
        val gameState = gameState.value
        val gameHistory = GameHistory(board = gameState.board, notes = gameState.notes)
        gameHistoryManager.addState(gameHistory)
    }

    private fun checkGameCompleted() {
        checkBoardSolved()
        val gameState = gameState.value
        isCompleted.update { checkGameCompletedUseCase.get().invoke(gameState.board, gameState.solvedBoard) }
    }

    private fun checkBoardSolved() {
        if (gameState.value.solvedBoard.isEmpty()) {
            val solvedBoard = solveBoardUseCase.get().invoke(
                boardEntity.board,
                boardEntity.type,
                gameState.value.initialBoard
            )
            gameState.update { it.copy(solvedBoard = solvedBoard) }
        }
    }

    private suspend fun saveGame() {
        val currentTimeMillis = System.currentTimeMillis()
        val state = gameState.value
        val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)
        if (savedGame != null) {
            updateSavedGameUseCase.get().invoke(
                savedGame.copy(
                    timer = state.timer,
                    board = state.board.convertToString(),
                    notes = state.notes.convertToString(),
                    actions = state.actions,
                    mistakes = state.mistakes,
                    lastPlayed = currentTimeMillis
                )
            )
        } else {
            insertSavedGameUseCase.get().invoke(
                uid = boardEntity.uid,
                board = state.board.convertToString(),
                notes = state.notes.convertToString(),
                timer = state.timer,
                actions = state.actions,
                mistakes = state.mistakes,
                lastPlayed = currentTimeMillis,
                startedAt = currentTimeMillis,
                finishedAt = 0L,
                status = GameStatus.IN_PROGRESS,
            )
        }
    }
}
