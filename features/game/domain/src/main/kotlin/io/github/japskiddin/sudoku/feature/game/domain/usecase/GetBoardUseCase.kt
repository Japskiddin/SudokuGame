package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.model.Board
import javax.inject.Inject

public class GetBoardUseCase
@Inject
constructor(
    private val boardRepository: BoardRepository
) {
    public suspend operator fun invoke(uid: Long): Board {
        return boardRepository.get(uid) ?: throw BoardNotFoundException("Board with $uid not found")
    }
}
