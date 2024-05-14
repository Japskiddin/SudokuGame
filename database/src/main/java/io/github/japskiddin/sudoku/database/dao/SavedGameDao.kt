package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.japskiddin.sudoku.database.model.BoardDBO
import io.github.japskiddin.sudoku.database.model.SavedGameDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGameDao {
  @Query("SELECT * FROM saved_game")
  fun getAll(): Flow<List<SavedGameDBO>>

  @Query("SELECT * FROM saved_game WHERE board_uid == :uid")
  suspend fun get(uid: Long): SavedGameDBO?

  @Query(
    "SELECT * FROM saved_game"
      + " JOIN board ON saved_game.board_uid == board.uid"
      + " ORDER BY uid DESC"
  )
  fun getSavedWithBoards(): Flow<Map<SavedGameDBO, BoardDBO>>

  @Query("SELECT * FROM saved_game ORDER BY board_uid DESC LIMIT 1")
  fun getLast(): Flow<SavedGameDBO>

  @Query(
    "SELECT * FROM saved_game"
      + " JOIN board ON saved_game.board_uid == board.uid"
      + " WHERE saved_game.status == 0"
      + " ORDER BY last_played DESC"
      + " LIMIT :limit"
  )
  fun getLastPlayable(limit: Int): Flow<Map<SavedGameDBO, BoardDBO>>

  @Insert
  suspend fun insert(savedGame: SavedGameDBO): Long

  @Insert
  suspend fun insert(savedGames: List<SavedGameDBO>): List<Long>

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun update(savedGame: SavedGameDBO)

  @Delete
  suspend fun delete(savedGame: SavedGameDBO)
}