package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.Board
import kotlinx.coroutines.flow.Flow

public interface BoardRepository {
    public fun getAll(): Flow<List<Board>>

    public suspend fun get(uid: Long): Board

    public suspend fun insert(board: Board): Long
}
