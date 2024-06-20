package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.japskiddin.sudoku.database.model.HistoryDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<HistoryDBO>>

    @Insert
    suspend fun insert(histories: List<HistoryDBO>)

    @Insert
    suspend fun insert(history: HistoryDBO)

    @Delete
    suspend fun remove(history: HistoryDBO)

    @Query("DELETE FROM history")
    suspend fun clean()
}
