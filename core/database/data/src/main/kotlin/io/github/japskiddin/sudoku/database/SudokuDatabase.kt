package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.HistoryDBO
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO

@Database(
    entities = [
        HistoryDBO::class,
        SavedGameDBO::class,
        BoardDBO::class
    ],
    version = 1
)
internal abstract class SudokuRoomDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    abstract fun boardDao(): BoardDao

    abstract fun savedGameDao(): SavedGameDao
}

public class SudokuDatabase internal constructor(
    private val database: SudokuRoomDatabase
) {
    public val historyDao: HistoryDao
        get() = database.historyDao()
    public val boardDao: BoardDao
        get() = database.boardDao()
    public val savedGameDao: SavedGameDao
        get() = database.savedGameDao()

    internal companion object {
        internal const val DATABASE_NAME = "sudoku"
    }
}

public fun SudokuDatabase(applicationContext: Context): SudokuDatabase {
    val sudokuRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        SudokuRoomDatabase::class.java,
        SudokuDatabase.DATABASE_NAME
    ).build()
    return SudokuDatabase(sudokuRoomDatabase)
}
