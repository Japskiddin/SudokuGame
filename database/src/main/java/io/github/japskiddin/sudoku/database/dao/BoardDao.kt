package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.japskiddin.sudoku.database.model.BoardDBO
import io.github.japskiddin.sudoku.database.model.SavedGameDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM board")
    fun getAll(): Flow<List<BoardDBO>>

    @Query("SELECT * from board WHERE difficulty == :gameDifficulty")
    fun getAll(gameDifficulty: Int): Flow<List<BoardDBO>>

    @Query("SELECT * from board")
    fun getAllList(): List<BoardDBO>

    @Query(
        "SELECT * FROM board"
            + " LEFT OUTER JOIN saved_game ON board.uid = saved_game.board_uid"
            + " WHERE difficulty == :difficulty"
            + " ORDER BY uid DESC"
    )
    fun getBoardsWithSavedGames(difficulty: Int): Flow<Map<BoardDBO, SavedGameDBO>>

    @Query("SELECT * FROM board WHERE uid == :uid")
    suspend fun get(uid: Long): BoardDBO?

    @Query("DELETE FROM board")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(boards: List<BoardDBO>): List<Long>

    @Insert
    suspend fun insert(board: BoardDBO): Long

    @Delete
    suspend fun delete(board: BoardDBO)

    @Delete
    suspend fun delete(boards: List<BoardDBO>)

    @Update
    suspend fun update(board: BoardDBO)

    @Update
    suspend fun update(boards: List<BoardDBO>)
}
