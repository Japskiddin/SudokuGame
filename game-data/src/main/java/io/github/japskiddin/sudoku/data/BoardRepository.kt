package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.model.BoardDBO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class BoardRepository @Inject constructor(private val boardDao: BoardDao) {
  fun getAll(): Flow<List<BoardDBO>> = boardDao.getAll()

  fun get(uid: Long) = boardDao.get(uid)
}