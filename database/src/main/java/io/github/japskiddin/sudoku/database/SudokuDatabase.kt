package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.RecordDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.database.model.BoardDBO
import io.github.japskiddin.sudoku.database.model.RecordDBO
import io.github.japskiddin.sudoku.database.model.SavedGameDBO
import io.github.japskiddin.sudoku.database.utils.DateConverter

@Database(
  entities = [
    RecordDBO::class,
    SavedGameDBO::class,
    BoardDBO::class,
  ],
  version = 1,
)
@TypeConverters(DateConverter::class)
internal abstract class SudokuRoomDatabase : RoomDatabase() {
  abstract fun recordDao(): RecordDao
  abstract fun boardDao(): BoardDao
  abstract fun savedGameDao(): SavedGameDao
}

class SudokuDatabase internal constructor(private val database: SudokuRoomDatabase) {
  val recordDao: RecordDao
    get() = database.recordDao()
  val boardDao: BoardDao
    get() = database.boardDao()
  val savedGameDao: SavedGameDao
    get() = database.savedGameDao()
}

fun SudokuDatabase(applicationContext: Context): SudokuDatabase {
  val sudokuRoomDatabase = Room.databaseBuilder(
    checkNotNull(applicationContext.applicationContext),
    SudokuRoomDatabase::class.java,
    "sudoku"
  ).build()
  return SudokuDatabase(sudokuRoomDatabase)
}