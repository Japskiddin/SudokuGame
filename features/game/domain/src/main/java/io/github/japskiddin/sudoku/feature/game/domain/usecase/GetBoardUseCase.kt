package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.data.BoardRepository
import javax.inject.Inject

internal class GetBoardUseCase
@Inject
constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(uid: Long) = boardRepository.get(uid)
}
