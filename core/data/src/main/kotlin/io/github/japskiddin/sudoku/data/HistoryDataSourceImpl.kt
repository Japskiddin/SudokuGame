package io.github.japskiddin.sudoku.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.domain.HistoryDataSource
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.data.utils.toHistory
import io.github.japskiddin.sudoku.data.utils.toHistoryDBO
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
public class HistoryDataSourceImpl(
    private val historyDao: HistoryDao
) : HistoryDataSource {
    override fun getAll(): Flow<List<History>> = historyDao.getAll().map { list ->
        list.map { historyDBO -> historyDBO.toHistory() }
    }

    override suspend fun insert(history: History): Long = historyDao.insert(history.toHistoryDBO())
}
