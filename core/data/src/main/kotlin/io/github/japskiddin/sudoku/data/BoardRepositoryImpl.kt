package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.BoardDataSource
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.model.Board
import javax.inject.Inject

public class BoardRepositoryImpl
@Inject
constructor(
    private val boardDataSource: BoardDataSource
) : BoardRepository {
    override suspend fun get(uid: Long): Board? = boardDataSource.get(uid)

    override suspend fun insert(board: Board): Long = boardDataSource.insert(board)
}
