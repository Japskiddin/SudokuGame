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
    @Query("SELECT * FROM board")
    public fun getAll(): Flow<List<BoardDBO>>

    @Query("SELECT * from board WHERE difficulty == :gameDifficulty")
    public fun getAll(gameDifficulty: Int): Flow<List<BoardDBO>>

    @Query("SELECT * from board")
    public suspend fun getAllList(): List<BoardDBO>

    @Query(
        "SELECT * FROM board" +
            " LEFT OUTER JOIN saved_game ON board.uid = saved_game.board_uid" +
            " WHERE difficulty == :difficulty" +
            " ORDER BY uid DESC"
    )
    public fun getBoardsWithSavedGames(difficulty: Int): Flow<Map<BoardDBO, SavedGameDBO>>

    @Query("SELECT * FROM board WHERE uid == :uid")
    public suspend fun get(uid: Long): BoardDBO?

    @Query("DELETE FROM board")
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
