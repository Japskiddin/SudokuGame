package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO
import kotlinx.coroutines.flow.Flow

@Dao
public interface SavedGameDao {
    @Query("SELECT * FROM saved_game WHERE board_uid == :boardUid")
    public suspend fun get(boardUid: Long): SavedGameDBO?

    @Query(
        """
        SELECT * FROM saved_game
        WHERE status = 0
        ORDER BY last_played DESC, board_uid DESC
        LIMIT 1
        """
    )
    public fun getLast(): Flow<SavedGameDBO?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public suspend fun insert(savedGame: SavedGameDBO): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun update(savedGame: SavedGameDBO)

    @Delete
    public suspend fun delete(savedGame: SavedGameDBO)
}
