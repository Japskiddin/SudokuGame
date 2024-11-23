package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import javax.inject.Inject

public class SolveBoardUseCase @Inject constructor() {
    public operator fun invoke(board: String, gameType: GameType, initialBoard: BoardList): BoardList {
        val qqWing = QQWingController()
        val size = gameType.size

        val boardToSolve = board.map { it.digitToInt(RADIX) }.toIntArray()
        val solvedArray = qqWing.solve(boardToSolve, gameType)

        val solvedBoard = List(size) { row ->
            List(size) { col ->
                BoardCell(row, col)
            }
        }
        for (i in 0 until size) {
            for (j in 0 until size) {
                solvedBoard[i][j].value = solvedArray[i * size + j]
            }
        }

        for (i in solvedBoard.indices) {
            for (j in solvedBoard.indices) {
                solvedBoard[i][j].isLocked = initialBoard[i][j].isLocked
            }
        }

        return solvedBoard
    }

    private companion object {
        private const val RADIX = 13
    }
}
