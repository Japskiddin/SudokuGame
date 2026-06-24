package io.github.japskiddin.sudoku.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.domain.HistoryDataSource
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.model.History
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
@Inject
public class HistoryRepositoryImpl(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {
    override fun getAll(): Flow<List<History>> = historyDataSource.getAll()

    override suspend fun insert(history: History): Long = historyDataSource.insert(history)
}
