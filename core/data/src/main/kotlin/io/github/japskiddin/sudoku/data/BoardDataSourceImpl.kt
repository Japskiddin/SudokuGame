package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.BoardDataSource
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.data.utils.toBoard
import io.github.japskiddin.sudoku.data.utils.toBoardDBO
import io.github.japskiddin.sudoku.database.dao.BoardDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class BoardDataSourceImpl
@Inject
constructor(
    private val boardDao: BoardDao
) : BoardDataSource {
    override fun getAll(): Flow<List<Board>> = boardDao.getAll().map { list ->
        list.map { boardDBO -> boardDBO.toBoard() }
    }

    override suspend fun get(uid: Long): Board? = boardDao.get(uid)?.toBoard()

    override suspend fun insert(board: Board): Long = boardDao.insert(board.toBoardDBO())
}
