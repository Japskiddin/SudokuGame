package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.RecordDataSource
import io.github.japskiddin.sudoku.core.model.Record
import io.github.japskiddin.sudoku.data.utils.toRecord
import io.github.japskiddin.sudoku.data.utils.toRecordDBO
import io.github.japskiddin.sudoku.database.dao.RecordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class RecordDataSourceImpl
@Inject
constructor(
    private val recordDao: RecordDao
) : RecordDataSource {
    override fun getAll(): Flow<List<Record>> = recordDao.getAll().map { list ->
        list.map { recordDBO -> recordDBO.toRecord() }
    }

    override suspend fun get(uid: Long): Record? = recordDao.get(uid)?.toRecord()

    override suspend fun insert(record: Record): Long = recordDao.insert(record.toRecordDBO())

    override suspend fun delete(record: Record): Unit = recordDao.delete(record.toRecordDBO())
}
