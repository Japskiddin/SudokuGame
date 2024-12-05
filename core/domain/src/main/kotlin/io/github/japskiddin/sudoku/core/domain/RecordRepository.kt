package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.Record
import kotlinx.coroutines.flow.Flow

public interface RecordRepository {
    public fun getAll(): Flow<List<Record>>

    public suspend fun get(uid: Long): Record?

    public suspend fun insert(record: Record): Long

    public suspend fun delete(record: Record)
}
