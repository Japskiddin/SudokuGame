package io.github.japskiddin.sudoku.feature.game.ui.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.game.utils.isValidCell
import io.github.japskiddin.sudoku.core.game.utils.isValidCellDynamic
import io.github.japskiddin.sudoku.core.game.utils.toImmutable
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.core.model.isEmpty
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.InsertSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.UpdateSavedGameUseCase
import io.github.japskiddin.sudoku.feature.game.ui.logic.utils.copyBoard
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
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

    public val uiState: StateFlow<UiState> = combine(isLoading, error, gameState) { isLoading, error, gameState ->
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
        value: Int
    ) {
        if (gameState.value.selectedCell.isEmpty()) return
        gameState.update { state ->
            val selectedCell = state.selectedCell.copy()
            val newBoard = copyBoard(state.board)
            val cell = newBoard[selectedCell.row][selectedCell.col]
            if (cell.isLocked) return
            cell.value = value

            if (value == 0) {
                cell.isError = false
                selectedCell.isError = false
            } else {
//                if (mistakesMethod == 0) {
                newBoard[cell.row][cell.col].isError =
                    !isValidCellDynamic(newBoard, newBoard[cell.row][cell.col], boardEntity.type)
                newBoard.forEach { cells ->
                    cells.forEach { cell ->
                        if (cell.value != 0 && cell.isError) {
                            cell.isError = !isValidCellDynamic(newBoard, cell, boardEntity.type)
                        }
                    }
                }
//                } else if (mistakesMethod == 1) {
//                val solvedBoard = gameState.value.solvedBoard
//                cell.isError = isValidCell(newBoard, solvedBoard, cell)
//            }

                selectedCell.isError = cell.isError
            }

            state.copy(board = newBoard.toImmutable(), selectedCell = selectedCell)
        }

        viewModelScope.launch(appDispatchers.io) {
            saveGame()
        }
    }

    public fun onBackButtonClick(): Unit = appNavigator.tryNavigateBack()

    public fun onUpdateSelectedBoardCell(boardCell: BoardCell) {
        gameState.update { it.copy(selectedCell = boardCell) }
    }

    private fun generateGameLevel() {
        val boardUid = (savedState.get<String>(Destination.KEY_BOARD_UID) ?: "-1").toLong()
        if (boardUid == -1L) {
            error.update { GameError.BOARD_NOT_FOUND }
            isLoading.update { false }
            return
        }

        val sudokuParser = SudokuParser()
        error.update { GameError.NONE }
        isLoading.update { true }

        viewModelScope.launch(appDispatchers.io) {
            boardEntity = try {
                getBoardUseCase.get().invoke(boardUid)
            } catch (ex: BoardNotFoundException) {
                error.update { ex.toGameError() }
                isLoading.update { false }
                return@launch
            }

            val savedGame = getSavedGameUseCase.get().invoke(boardEntity.uid)
            val initialBoard = sudokuParser.parseBoard(boardEntity.initialBoard, boardEntity.type)
            initialBoard.forEach { cells ->
                cells.forEach { cell ->
                    cell.isLocked = cell.value != 0
                }
            }
            gameState.update { it.copy(initialBoard = initialBoard.toImmutable()) }

            if (boardEntity.solvedBoard.isNotBlank() && !boardEntity.solvedBoard.contains("0")) {
                val solvedBoard = sudokuParser.parseBoard(boardEntity.solvedBoard, boardEntity.type)
                gameState.update { it.copy(solvedBoard = solvedBoard.toImmutable()) }
            } else {
                solveBoard()
            }

            if (savedGame != null) {
                restoreSavedGame(savedGame)
            } else {
                gameState.update { it.copy(board = initialBoard.toImmutable()) }
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

    private fun restoreSavedGame(savedGame: SavedGame) {
        val sudokuParser = SudokuParser()
        val gameBoard = sudokuParser.parseBoard(savedGame.currentBoard, boardEntity.type)
        val initialBoard = gameState.value.initialBoard
        for (i in gameBoard.indices) {
            for (j in gameBoard.indices) {
                gameBoard[i][j].isLocked = initialBoard[i][j].isLocked
                if (gameBoard[i][j].value != 0 && !gameBoard[i][j].isLocked) {
//                    if (mistakesMethod.value == 1) {
//                        gameBoard[i][j].error =
//                            !isValidCellDynamic(
//                                board = gameBoard,
//                                cell = gameBoard[i][j],
//                                type = boardEntity.type
//                            )
//                    } else {
                    val solvedBoard = gameState.value.solvedBoard
                    gameBoard[i][j].isError = isValidCell(gameBoard, solvedBoard, gameBoard[i][j])
//                    }
                }
            }
        }

        gameState.update { it.copy(board = gameBoard.toImmutable()) }
    }

    private fun solveBoard() {
        val qqWing = QQWingController()
        val size = boardEntity.type.size
        val radix = 13
        val boardToSolve = boardEntity.initialBoard.map { it.digitToInt(radix) }.toIntArray()
        val solved = qqWing.solve(boardToSolve, boardEntity.type)

        val newSolvedBoard = MutableList(size) { row ->
            MutableList(size) { col ->
                BoardCell(
                    row,
                    col,
                    0
                )
            }
        }
        for (i in 0 until size) {
            for (j in 0 until size) {
                newSolvedBoard[i][j].value = solved[i * size + j]
            }
        }

        viewModelScope.launch(appDispatchers.io) {
            val sudokuParser = SudokuParser()
//            updateBoardUseCase(boardEntity.copy(solvedBoard = sudokuParser.boardToString(newSolvedBoard)))
        }

        val initialBoard = gameState.value.initialBoard
        for (i in newSolvedBoard.indices) {
            for (j in newSolvedBoard.indices) {
                newSolvedBoard[i][j].isLocked = initialBoard[i][j].isLocked
            }
        }

        gameState.update { it.copy(solvedBoard = newSolvedBoard.toImmutable()) }
    }
}
