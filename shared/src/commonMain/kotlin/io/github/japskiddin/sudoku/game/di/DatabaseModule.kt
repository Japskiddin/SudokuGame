package io.github.japskiddin.sudoku.game.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao

@ContributesTo(AppScope::class)
public interface DatabaseModule {
    @Provides
    public fun provideHistoryDao(database: SudokuDatabase): HistoryDao = database.historyDao

    @Provides
    public fun provideBoardDao(database: SudokuDatabase): BoardDao = database.boardDao

    @Provides
    public fun provideSavedGameDao(database: SudokuDatabase): SavedGameDao = database.savedGameDao
}
