package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.convertToString
import kotlinx.coroutines.withContext
import javax.inject.Inject

public class GenerateSudokuUseCase
@Inject
constructor(
    private val appDispatchers: AppDispatchers,
) {
    public suspend operator fun invoke(
        mode: GameMode
    ): Board = withContext(appDispatchers.default) {
        val type = mode.type
        val difficulty = mode.difficulty
        val boardSize = type.size
        val puzzle = List(boardSize) { row ->
            List(boardSize) { col ->
                BoardCell(row, col)
            }
        }
        val solvedPuzzle = List(boardSize) { row ->
            List(boardSize) { col ->
                BoardCell(row, col)
            }
        }
        val qqWingController = QQWingController()

        val generatedBoard: IntArray
        val solvedBoard: IntArray
        try {
            generatedBoard = qqWingController.generate(type, difficulty) ?: throw SudokuNotGeneratedException()
            solvedBoard = qqWingController.solve(generatedBoard, type)
        } catch (_: IllegalArgumentException) {
            throw SudokuNotGeneratedException()
        }

        if (qqWingController.isImpossible || qqWingController.solutionCount != 1) {
            throw SudokuNotGeneratedException()
        }

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                puzzle[i][j].value = generatedBoard[i * boardSize + j]
                solvedPuzzle[i][j].value = solvedBoard[i * boardSize + j]
            }
        }

        Board(
            board = puzzle.convertToString(),
            solvedBoard = solvedPuzzle.convertToString(),
            difficulty = difficulty,
            type = type
        )
    }
}
