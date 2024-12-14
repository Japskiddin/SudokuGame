package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.RecordDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface RecordDao {
    @Query("SELECT * FROM ${RecordDBO.TABLE} WHERE ${RecordDBO.COLUMN_BOARD_UID} == :uid")
    public suspend fun get(uid: Long): RecordDBO

    @Query("SELECT * from ${RecordDBO.TABLE}")
    public fun getAll(): Flow<List<RecordDBO>>

    @Query(
        "SELECT * FROM ${RecordDBO.TABLE}" +
            " JOIN ${BoardDBO.TABLE} ON ${RecordDBO.COLUMN_BOARD_UID} == ${BoardDBO.COLUMN_UID}" +
            " WHERE ${BoardDBO.COLUMN_TYPE} == :type AND ${BoardDBO.COLUMN_DIFFICULTY} == :difficulty" +
            " ORDER BY ${RecordDBO.COLUMN_TIME} ASC"
    )
    public fun getAll(
        difficulty: Int,
        type: Int
    ): Flow<List<RecordDBO>>

    @Query("SELECT * FROM ${RecordDBO.TABLE} ORDER BY ${RecordDBO.COLUMN_TIME} ASC")
    public fun getAllSortedByTime(): Flow<List<RecordDBO>>

    @Insert
    public suspend fun insert(record: RecordDBO): Long

    @Insert
    public suspend fun insert(records: List<RecordDBO>): List<Long>

    @Delete
    public suspend fun delete(record: RecordDBO)
}
