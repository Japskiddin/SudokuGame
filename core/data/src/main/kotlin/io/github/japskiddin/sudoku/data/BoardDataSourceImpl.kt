package io.github.japskiddin.sudoku.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.domain.BoardDataSource
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.data.utils.toBoard
import io.github.japskiddin.sudoku.data.utils.toBoardDBO
import io.github.japskiddin.sudoku.database.dao.BoardDao

@ContributesBinding(AppScope::class)
@Inject
public class BoardDataSourceImpl(
    private val boardDao: BoardDao
) : BoardDataSource {
    override suspend fun get(uid: Long): Board? = boardDao.get(uid)?.toBoard()

    override suspend fun insert(board: Board): Long = boardDao.insert(board.toBoardDBO())
}
