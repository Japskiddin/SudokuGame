package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao

@BindingContainer
object DatabaseModule {
    @SingleIn(AppScope::class)
    @Provides
    fun provideDatabase(application: Context): SudokuDatabase = SudokuDatabase(application)

    @Provides
    fun provideHistoryDao(database: SudokuDatabase): HistoryDao = database.historyDao

    @Provides
    fun provideBoardDao(database: SudokuDatabase): BoardDao = database.boardDao

    @Provides
    fun provideSavedGameDao(database: SudokuDatabase): SavedGameDao = database.savedGameDao
}
