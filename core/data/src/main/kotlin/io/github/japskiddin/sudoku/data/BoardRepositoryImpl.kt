package io.github.japskiddin.sudoku.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.domain.BoardDataSource
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.model.Board

@ContributesBinding(AppScope::class)
@Inject
public class BoardRepositoryImpl(
    private val boardDataSource: BoardDataSource
) : BoardRepository {
    override suspend fun get(uid: Long): Board? = boardDataSource.get(uid)

    override suspend fun insert(board: Board): Long = boardDataSource.insert(board)
}
