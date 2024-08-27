package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.data.utils.toBoard
import io.github.japskiddin.sudoku.data.utils.toBoardDBO
import io.github.japskiddin.sudoku.database.dao.BoardDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public interface BoardRepository {
    public fun getAll(): Flow<List<Board>>
    public suspend fun get(uid: Long): Board
    public suspend fun insert(board: Board): Long
}

public class BoardRepositoryImpl
@Inject
constructor(
    private val boardDao: BoardDao
) : BoardRepository {
    override fun getAll(): Flow<List<Board>> =
        boardDao.getAll().map { list ->
            list.map { boardDBO -> boardDBO.toBoard() }
        }

    override suspend fun get(uid: Long): Board =
        boardDao.get(uid)?.toBoard()
            ?: throw BoardNotFoundException("Board with $uid not found")

    override suspend fun insert(board: Board): Long = boardDao.insert(board.toBoardDBO())
}

public class BoardNotFoundException(message: String) : Exception(message)
