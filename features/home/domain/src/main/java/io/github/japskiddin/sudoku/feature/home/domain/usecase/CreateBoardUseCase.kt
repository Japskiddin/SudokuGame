package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.data.BoardRepository
import io.github.japskiddin.sudoku.data.model.Board
import javax.inject.Inject

internal class CreateBoardUseCase
@Inject
constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(board: Board) = boardRepository.insert(board)
}
