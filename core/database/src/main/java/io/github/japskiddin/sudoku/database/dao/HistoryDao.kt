package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.japskiddin.sudoku.database.model.HistoryDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history")
    public fun getAll(): Flow<List<HistoryDBO>>

    @Insert
    public suspend fun insert(histories: List<HistoryDBO>)

    @Insert
    public suspend fun insert(history: HistoryDBO)

    @Delete
    public suspend fun remove(history: HistoryDBO)

    @Query("DELETE FROM history")
    public suspend fun clean()
}
