package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.RecordDataSource
import io.github.japskiddin.sudoku.core.domain.RecordRepository
import io.github.japskiddin.sudoku.core.model.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class RecordRepositoryImpl
@Inject
constructor(
    private val recordDataSource: RecordDataSource
) : RecordRepository {
    override fun getAll(): Flow<List<Record>> = recordDataSource.getAll()

    override suspend fun get(uid: Long): Record? = recordDataSource.get(uid)

    override suspend fun insert(record: Record): Long = recordDataSource.insert(record)

    override suspend fun delete(record: Record): Unit = recordDataSource.delete(record)
}
