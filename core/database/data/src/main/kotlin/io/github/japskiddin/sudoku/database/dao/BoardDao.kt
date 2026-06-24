package io.github.japskiddin.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.japskiddin.sudoku.database.entities.BoardDBO

@Dao
public interface BoardDao {
    @Query("SELECT * FROM board WHERE uid == :uid")
    public suspend fun get(uid: Long): BoardDBO?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public suspend fun insert(board: BoardDBO): Long
}
