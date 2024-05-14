package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.model.HistoryDBO
import io.github.japskiddin.sudoku.database.utils.Converters

class GameDatabase internal constructor(private val database: GameRoomDatabase) {
  val historyDao: HistoryDao
    get() = database.historyDao()
}

@Database(entities = [HistoryDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class GameRoomDatabase : RoomDatabase() {
  abstract fun historyDao(): HistoryDao
}

fun GameDatabase(applicationContext: Context): GameDatabase {
  val gameRoomDatabase = Room.databaseBuilder(
    checkNotNull(applicationContext.applicationContext),
    GameRoomDatabase::class.java,
    "game"
  ).build()
  return GameDatabase(gameRoomDatabase)
}