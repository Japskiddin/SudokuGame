package io.github.japskiddin.sudoku.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

public fun SudokuDatabase(path: String): SudokuDatabase = SudokuDatabase(
    Room.databaseBuilder<SudokuRoomDatabase>(name = path)
        .setDriver(BundledSQLiteDriver())
        .build()
)
