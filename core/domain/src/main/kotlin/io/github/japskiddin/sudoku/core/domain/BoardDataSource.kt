package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.Board

public interface BoardDataSource {
    public suspend fun get(uid: Long): Board?

    public suspend fun insert(board: Board): Long
}
