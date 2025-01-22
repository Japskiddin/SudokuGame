package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.game.utils.isValidCell
import io.github.japskiddin.sudoku.core.game.utils.isValidCellDynamic
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.MistakesMethod
import kotlinx.coroutines.withContext
import javax.inject.Inject

public class RestoreGameUseCase
@Inject
constructor(
    private val appDispatchers: AppDispatchers,
) {
    public suspend operator fun invoke(
        restoredBoard: BoardList,
        type: GameType,
        initialBoard: BoardList,
        solvedBoard: BoardList,
        mistakesMethod: MistakesMethod = MistakesMethod.CLASSIC
    ): BoardList = withContext(appDispatchers.default) {
        val board = restoredBoard.toList()
        for (i in board.indices) {
            for (j in board.indices) {
                restoreCell(
                    cell = board[i][j],
                    initialCell = initialBoard[i][j],
                    mistakesMethod = mistakesMethod,
                    type = type,
                    restoredBoard = board,
                    solvedBoard = solvedBoard
                )
            }
        }
        board
    }

    private fun restoreCell(
        cell: BoardCell,
        initialCell: BoardCell,
        mistakesMethod: MistakesMethod,
        type: GameType,
        restoredBoard: BoardList,
        solvedBoard: BoardList
    ) {
        cell.isLocked = initialCell.isLocked
        if (cell.value != 0 && !cell.isLocked) {
            cell.isError = when (mistakesMethod) {
                MistakesMethod.MODERN -> !isValidCellDynamic(
                    board = restoredBoard,
                    cell = cell,
                    type = type
                )

                MistakesMethod.CLASSIC -> isValidCell(restoredBoard, solvedBoard, cell)
            }
        }
    }
}
