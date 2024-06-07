package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.data.BoardRepository
import io.github.japskiddin.sudoku.data.SavedGameRepository
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.AppNavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSudokuDatabase(@ApplicationContext context: Context): SudokuDatabase {
        return SudokuDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBoardRepository(boardDao: BoardDao): BoardRepository = BoardRepository(boardDao)

    @Provides
    @Singleton
    fun provideBoardDao(sudokuDatabase: SudokuDatabase): BoardDao = sudokuDatabase.boardDao

    @Provides
    @Singleton
    fun provideSavedGameRepository(savedGameDao: SavedGameDao): SavedGameRepository =
        SavedGameRepository(savedGameDao)

    @Provides
    @Singleton
    fun provideSavedGameDao(sudokuDatabase: SudokuDatabase): SavedGameDao =
        sudokuDatabase.savedGameDao

    // TODO: Wrapper class for Datastore?
    @Provides
    @Singleton
    fun provideSettingsDatastore(@ApplicationContext context: Context): SettingsDatastore {
        return SettingsDatastore(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigatorImpl()
}
