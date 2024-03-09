package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.models.HistoryDBO
import io.github.japskiddin.sudoku.database.utils.Converters

@Database(entities = [HistoryDBO::class], version = 1)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

fun GameDatabase(applicationContext: Context): GameDatabase {
    return Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        GameDatabase::class.java,
        "game"
    ).build()
}