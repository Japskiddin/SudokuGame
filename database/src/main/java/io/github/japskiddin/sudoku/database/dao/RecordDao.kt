package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.japskiddin.sudoku.database.model.RecordDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM record WHERE board_uid == :uid")
    suspend fun get(uid: Long): RecordDBO

    @Query("SELECT * from record")
    fun getAll(): Flow<List<RecordDBO>>

    @Query(
        "SELECT * FROM record"
            + " JOIN board ON record.board_uid == board.uid"
            + " WHERE type == :type AND difficulty == :difficulty"
            + " ORDER BY time ASC"
    )
    fun getAll(difficulty: Int, type: Int): Flow<List<RecordDBO>>

    @Query("SELECT * FROM record ORDER BY time ASC")
    fun getAllSortedByTime(): Flow<List<RecordDBO>>

    @Insert
    suspend fun insert(record: RecordDBO): Long

    @Insert
    suspend fun insert(records: List<RecordDBO>): List<Long>

    @Delete
    suspend fun delete(record: RecordDBO)
}
