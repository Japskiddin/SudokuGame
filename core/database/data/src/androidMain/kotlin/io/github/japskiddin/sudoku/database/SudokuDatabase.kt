package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room

public fun SudokuDatabase(applicationContext: Context): SudokuDatabase {
    val sudokuRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        SudokuRoomDatabase::class.java,
        SudokuDatabase.DATABASE_NAME
    ).build()
    return SudokuDatabase(sudokuRoomDatabase)
}
