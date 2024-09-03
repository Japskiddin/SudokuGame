package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import javax.inject.Inject

public class GenerateSudokuUseCase @Inject constructor() {
    public operator fun invoke(
        type: GameType,
        difficulty: GameDifficulty
    ): Board {
        val boardSize = type.size
        val puzzle = List(boardSize) { row ->
            List(boardSize) { col -> BoardCell(row, col, 0) }
        }
        val solvedPuzzle = List(boardSize) { row ->
            List(boardSize) { col -> BoardCell(row, col, 0) }
        }
        val qqWingController = QQWingController()
        val generatedBoard = qqWingController.generate(type, difficulty) ?: throw SudokuNotGeneratedException()
        val solvedBoard = qqWingController.solve(generatedBoard, type)

        if (qqWingController.isImpossible || qqWingController.solutionCount != 1) {
            throw SudokuNotGeneratedException()
        }

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                puzzle[i][j].value = generatedBoard[i * boardSize + j]
                solvedPuzzle[i][j].value = solvedBoard[i * boardSize + j]
            }
        }

        val sudokuParser = SudokuParser()
        return Board(
            initialBoard = sudokuParser.boardToString(puzzle),
            solvedBoard = sudokuParser.boardToString(solvedPuzzle),
            difficulty = difficulty,
            type = type
        )
    }
}

public class SudokuNotGeneratedException(message: String = "Error with generating sudoku") : Exception(message)
