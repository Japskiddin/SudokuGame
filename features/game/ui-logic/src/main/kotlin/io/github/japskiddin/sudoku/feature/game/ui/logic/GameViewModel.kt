package io.github.japskiddin.sudoku.feature.game.ui.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.feature.utils.toGameError
import io.github.japskiddin.sudoku.core.game.utils.convertToList
import io.github.japskiddin.sudoku.core.game.utils.convertToString
import io.github.japskiddin.sudoku.core.game.utils.initiate
import io.github.japskiddin.sudoku.core.game.utils.isSudokuFilled
import io.github.japskiddin.sudoku.core.game.utils.isValidCell
import io.github.japskiddin.sudoku.core.game.utils.isValidCellDynamic
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.MistakesMethod
import io.github.japskiddin.sudoku.core.model.isEmpty
import io.github.japskiddin.sudoku.feature.game.domain.usecase.CheckGameCompletedUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.InsertSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.RestoreGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.SolveBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.UpdateSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.asUiState
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.copyBoard
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private lateinit var gameHistoryManager: GameHistoryManager

    private val gameState = MutableStateFlow(GameState.Initial)

    private var timerJob: Job? = null
    private var boardUid: Long = -1L

    public val uiState: StateFlow<UiState> = gameState
        .asUiState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Initial)

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
            is UiAction.CloseError -> appNavigator.tryNavigateBack()
        }
    }

    public fun onBackPressed() {
        viewModelScope.launch(appDispatchers.io) {
            saveGame()
        }
        appNavigator.tryNavigateBack()
    }

    private fun generateGameLevel() {
        gameState.update { it.copy(status = GameState.Status.LOADING) }

        boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
        if (boardUid == -1L) {
            gameState.update { it.copy(error = GameError.BOARD_NOT_FOUND) }
            return
        }

        viewModelScope.launch(appDispatchers.io) {
            val boardEntity = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (ex: BoardNotFoundException) {
                gameState.update { it.copy(error = ex.toGameError()) }
                return@launch
            }

            val boardType = boardEntity.type
            val boardDifficulty = boardEntity.difficulty
            val board = boardEntity.board

            // TODO: перенести метод расширения в Board
            val initialBoard = board.convertToList(boardType)
            initialBoard.initiate()

            val solvedBoard = if (boardEntity.solvedBoard.isSudokuFilled()) {
                boardEntity.solvedBoard.convertToList(boardType)
            } else {
                solveBoardUseCase.get().invoke(board, boardType, initialBoard)
            }

            val savedGame = getSavedGameUseCase.get().invoke(boardUid)
            if (savedGame != null) {
                val savedBoard = savedGame.board.convertToList(boardType)
                val restoredBoard = restoreGameUseCase.get().invoke(
                    savedBoard,
                    boardType,
                    initialBoard,
                    solvedBoard
                )
                gameState.update {
                    it.copy(
                        board = restoredBoard,
                        actions = savedGame.actions,
                        mistakes = savedGame.mistakes,
                        time = savedGame.time
                    )
                }
            } else {
                saveGame()
                gameState.update { it.copy(board = initialBoard) }
            }

            gameState.update {
                it.copy(
                    status = GameState.Status.PLAYING,
                    type = boardType,
                    difficulty = boardDifficulty,
                    initialBoard = initialBoard,
                    solvedBoard = solvedBoard
                )
            }

            gameHistoryManager = GameHistoryManager(GameHistory(board = gameState.value.board, notes = listOf()))
            startTimer()
        }
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch(appDispatchers.io) {
            while (true) {
                delay(TIMER_DELAY)
                gameState.update { it.copy(time = it.time + 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
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
                            !isValidCellDynamic(newBoard, newBoard[cell.row][cell.col], state.type)
                        newBoard.forEach { cells ->
                            cells.forEach { cell ->
                                if (cell.value != 0 && cell.isError) {
                                    cell.isError = !isValidCellDynamic(newBoard, cell, state.type)
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
        val isCompleted = checkGameCompletedUseCase.get().invoke(gameState.value.board, gameState.value.solvedBoard)
        if (isCompleted) {
            gameState.update { it.copy(status = GameState.Status.COMPLETED) }
            stopTimer()
        }
    }

    private fun checkBoardSolved() {
        val state = gameState.value
        if (gameState.value.solvedBoard.isEmpty()) {
            val solvedBoard = solveBoardUseCase.get().invoke(
                state.board.convertToString(),
                state.type,
                gameState.value.initialBoard
            )
            gameState.update { it.copy(solvedBoard = solvedBoard) }
        }
    }

    private suspend fun saveGame() {
        val currentTimeMillis = System.currentTimeMillis()
        val state = gameState.value
        val savedGame = getSavedGameUseCase.get().invoke(boardUid)
        if (savedGame != null) {
            updateSavedGameUseCase.get().invoke(
                savedGame.copy(
                    time = state.time,
                    board = state.board.convertToString(),
                    notes = state.notes.convertToString(),
                    actions = state.actions,
                    mistakes = state.mistakes,
                    lastPlayed = currentTimeMillis
                )
            )
        } else {
            insertSavedGameUseCase.get().invoke(
                uid = boardUid,
                board = state.board.convertToString(),
                notes = state.notes.convertToString(),
                time = state.time,
                actions = state.actions,
                mistakes = state.mistakes,
                lastPlayed = currentTimeMillis,
                startedAt = currentTimeMillis,
                finishedAt = 0L,
                status = GameStatus.IN_PROGRESS,
            )
        }
    }

    private companion object {
        private const val TIMER_DELAY = 1000L
    }
}
