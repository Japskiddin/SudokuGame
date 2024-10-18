package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface SavedGameDao {
    @Query("SELECT * FROM ${SavedGameDBO.TABLE}")
    public fun getAll(): Flow<List<SavedGameDBO>>

    @Query("SELECT * FROM ${SavedGameDBO.TABLE} WHERE ${SavedGameDBO.COLUMN_BOARD_UID} == :boardUid")
    public suspend fun get(boardUid: Long): SavedGameDBO?

    @Query(
        "SELECT * FROM ${SavedGameDBO.TABLE}" +
            " JOIN ${BoardDBO.TABLE} ON ${SavedGameDBO.COLUMN_BOARD_UID} == ${BoardDBO.COLUMN_UID}" +
            " ORDER BY ${SavedGameDBO.COLUMN_BOARD_UID} DESC"
    )
    public fun getSavedWithBoards(): Flow<Map<SavedGameDBO, BoardDBO>>

    @Query("SELECT * FROM ${SavedGameDBO.TABLE} ORDER BY ${SavedGameDBO.COLUMN_BOARD_UID} DESC LIMIT 1")
    public fun getLast(): Flow<SavedGameDBO?>

    @Query(
        "SELECT * FROM ${SavedGameDBO.TABLE}" +
            " JOIN ${BoardDBO.TABLE} ON ${SavedGameDBO.COLUMN_BOARD_UID} == ${BoardDBO.COLUMN_UID}" +
            " WHERE ${SavedGameDBO.COLUMN_STATUS} == 0" +
            " ORDER BY ${SavedGameDBO.COLUMN_LAST_PLAYED} DESC" +
            " LIMIT :limit"
    )
    public fun getLastPlayable(limit: Int): Flow<Map<SavedGameDBO, BoardDBO>>

    @Insert
    public suspend fun insert(savedGame: SavedGameDBO): Long

    @Insert
    public suspend fun insert(savedGames: List<SavedGameDBO>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun update(savedGame: SavedGameDBO)

    @Delete
    public suspend fun delete(savedGame: SavedGameDBO)
}
