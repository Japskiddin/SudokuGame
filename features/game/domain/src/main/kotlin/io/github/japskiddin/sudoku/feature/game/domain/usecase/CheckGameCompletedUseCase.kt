package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.game.utils.BoardList
import javax.inject.Inject

public class CheckGameCompletedUseCase @Inject constructor() {
    public operator fun invoke(board: BoardList, solvedBoard: BoardList): Boolean {
        for (i in solvedBoard.indices) {
            for (j in solvedBoard.indices) {
                if (solvedBoard[i][j].value != board[i][j].value) return false
            }
        }

        return true
    }
}
