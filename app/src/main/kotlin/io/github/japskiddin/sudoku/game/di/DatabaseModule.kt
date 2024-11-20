package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSudokuDatabase(
        @ApplicationContext context: Context
    ): SudokuDatabase = SudokuDatabase(context)

    @Provides
    fun provideBoardDao(
        sudokuDatabase: SudokuDatabase
    ): BoardDao = sudokuDatabase.boardDao

    @Provides
    fun provideSavedGameDao(
        sudokuDatabase: SudokuDatabase
    ): SavedGameDao = sudokuDatabase.savedGameDao
}
