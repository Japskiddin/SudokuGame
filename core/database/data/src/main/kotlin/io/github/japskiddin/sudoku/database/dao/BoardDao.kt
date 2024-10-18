package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
@Dao
public interface BoardDao {
    @Query("SELECT * FROM ${BoardDBO.TABLE}")
    public fun getAll(): Flow<List<BoardDBO>>

    @Query("SELECT * from ${BoardDBO.TABLE} WHERE ${BoardDBO.COLUMN_DIFFICULTY} == :difficulty")
    public fun getAll(difficulty: Int): Flow<List<BoardDBO>>

    @Query(
        "SELECT * FROM ${BoardDBO.TABLE}" +
            " LEFT OUTER JOIN ${SavedGameDBO.TABLE} ON ${BoardDBO.COLUMN_UID} = ${SavedGameDBO.COLUMN_BOARD_UID}" +
            " WHERE ${BoardDBO.COLUMN_DIFFICULTY} == :difficulty" +
            " ORDER BY ${BoardDBO.COLUMN_UID} DESC"
    )
    public fun getBoardsWithSavedGames(difficulty: Int): Flow<Map<BoardDBO, SavedGameDBO>>

    @Query("SELECT * FROM ${BoardDBO.TABLE} WHERE ${BoardDBO.COLUMN_UID} == :uid")
    public suspend fun get(uid: Long): BoardDBO?

    @Query("DELETE FROM ${BoardDBO.TABLE}")
    public suspend fun deleteAll()

    @Insert
    public suspend fun insert(boards: List<BoardDBO>): List<Long>

    @Insert
    public suspend fun insert(board: BoardDBO): Long

    @Delete
    public suspend fun delete(board: BoardDBO)

    @Delete
    public suspend fun delete(boards: List<BoardDBO>)

    @Update
    public suspend fun update(board: BoardDBO)

    @Update
    public suspend fun update(boards: List<BoardDBO>)
}
