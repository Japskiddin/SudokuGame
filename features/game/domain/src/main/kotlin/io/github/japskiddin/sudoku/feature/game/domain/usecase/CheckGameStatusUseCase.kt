package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

public class CheckGameStatusUseCase
@Inject
constructor(
    private val appDispatchers: AppDispatchers,
) {
    public suspend operator fun invoke(
        board: BoardList,
        solvedBoard: BoardList,
        mistakes: Int,
        difficulty: GameDifficulty
    ): GameStatus {
        return withContext(appDispatchers.default) {
            when {
                isGameFailed(mistakes, difficulty) -> GameStatus.FAILED

                isGameCompleted(board, solvedBoard) -> GameStatus.COMPLETED

                else -> GameStatus.IN_PROGRESS
            }
        }
    }

    private fun isGameFailed(mistakes: Int, difficulty: GameDifficulty): Boolean = mistakes >= difficulty.mistakesLimit

    private fun isGameCompleted(board: BoardList, solvedBoard: BoardList): Boolean {
        for (i in solvedBoard.indices) {
            for (j in solvedBoard.indices) {
                if (solvedBoard[i][j].value != board[i][j].value) return false
            }
        }

        return true
    }
}
