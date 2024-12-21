package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.HistoryDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM ${HistoryDBO.TABLE} WHERE ${HistoryDBO.COLUMN_BOARD_UID} == :uid")
    public suspend fun get(uid: Long): HistoryDBO

    @Query("SELECT * from ${HistoryDBO.TABLE}")
    public fun getAll(): Flow<List<HistoryDBO>>

    @Query(
        "SELECT * FROM ${HistoryDBO.TABLE}" +
            " JOIN ${BoardDBO.TABLE} ON ${HistoryDBO.COLUMN_BOARD_UID} == ${BoardDBO.COLUMN_UID}" +
            " WHERE ${BoardDBO.COLUMN_TYPE} == :type AND ${BoardDBO.COLUMN_DIFFICULTY} == :difficulty" +
            " ORDER BY ${HistoryDBO.COLUMN_TIME} ASC"
    )
    public fun getAll(
        difficulty: Int,
        type: Int
    ): Flow<List<HistoryDBO>>

    @Query("SELECT * FROM ${HistoryDBO.TABLE} ORDER BY ${HistoryDBO.COLUMN_TIME} ASC")
    public fun getAllSortedByTime(): Flow<List<HistoryDBO>>

    @Insert
    public suspend fun insert(history: HistoryDBO): Long

    @Insert
    public suspend fun insert(history: List<HistoryDBO>): List<Long>

    @Delete
    public suspend fun delete(history: HistoryDBO)
}
