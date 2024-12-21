package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.HistoryDataSource
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.model.History
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class HistoryRepositoryImpl
@Inject
constructor(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {
    override fun getAll(): Flow<List<History>> = historyDataSource.getAll()

    override suspend fun get(uid: Long): History? = historyDataSource.get(uid)

    override suspend fun insert(history: History): Long = historyDataSource.insert(history)

    override suspend fun delete(history: History): Unit = historyDataSource.delete(history)
}
