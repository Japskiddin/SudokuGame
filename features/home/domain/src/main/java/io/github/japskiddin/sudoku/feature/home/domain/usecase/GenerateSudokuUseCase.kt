package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType
import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.data.model.Board
import javax.inject.Inject

internal class GenerateSudokuUseCase @Inject constructor() {
    operator fun invoke(
        type: GameType = GameType.DEFAULT9X9,
        difficulty: GameDifficulty = GameDifficulty.INTERMEDIATE
    ): Board {
        val sudokuParser = SudokuParser()
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

        return Board(
            initialBoard = sudokuParser.boardToString(puzzle),
            solvedBoard = sudokuParser.boardToString(solvedPuzzle),
            difficulty = difficulty,
            type = type
        )
    }
}

internal class SudokuNotGeneratedException(message: String = "Error with generating sudoku") : Exception(message)
