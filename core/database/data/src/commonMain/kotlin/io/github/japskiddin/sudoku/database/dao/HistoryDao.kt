package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.japskiddin.sudoku.database.entities.HistoryDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface HistoryDao {
    @Query("SELECT * from history")
    public fun getAll(): Flow<List<HistoryDBO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public suspend fun insert(history: HistoryDBO): Long
}
