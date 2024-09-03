package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.core.common.AndroidLogcatLogger
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.Logger
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.data.BoardRepositoryImpl
import io.github.japskiddin.sudoku.data.SavedGameRepositoryImpl
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
    fun provideSudokuDatabase(
        @ApplicationContext context: Context
    ): SudokuDatabase {
        return SudokuDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBoardRepository(boardDao: BoardDao): BoardRepository = BoardRepositoryImpl(boardDao)

    @Provides
    @Singleton
    fun provideBoardDao(sudokuDatabase: SudokuDatabase): BoardDao = sudokuDatabase.boardDao

    @Provides
    @Singleton
    fun provideSavedGameRepository(savedGameDao: SavedGameDao): SavedGameRepository =
        SavedGameRepositoryImpl(savedGameDao)

    @Provides
    @Singleton
    fun provideSavedGameDao(sudokuDatabase: SudokuDatabase): SavedGameDao = sudokuDatabase.savedGameDao

    @Provides
    @Singleton
    fun provideSettingsDatastore(
        @ApplicationContext context: Context
    ): SettingsDatastore {
        return SettingsDatastore(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigatorImpl()

    @Provides
    fun provideLogger(): Logger = AndroidLogcatLogger()
}
