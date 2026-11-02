package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.History
import kotlinx.coroutines.flow.Flow

public interface HistoryDataSource {
    public fun getAll(): Flow<List<History>>

    public suspend fun insert(history: History): Long
}
