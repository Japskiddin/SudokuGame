package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.SudokuNotGeneratedException
import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import kotlinx.coroutines.withContext
import javax.inject.Inject

public class SolveBoardUseCase
@Inject
constructor(
    private val appDispatchers: AppDispatchers,
) {
    public suspend operator fun invoke(board: String, gameType: GameType, initialBoard: BoardList): BoardList =
        withContext(appDispatchers.default) {
            val qqWing = QQWingController()
            val size = gameType.size

            val boardToSolve = board.map { it.digitToInt(RADIX) }.toIntArray()
            val solvedArray = try {
                qqWing.solve(boardToSolve, gameType)
            } catch (_: IllegalArgumentException) {
                throw SudokuNotGeneratedException()
            }

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

            solvedBoard
        }

    private companion object {
        private const val RADIX = 13
    }
}
