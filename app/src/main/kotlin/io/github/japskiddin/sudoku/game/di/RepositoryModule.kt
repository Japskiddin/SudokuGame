package io.github.japskiddin.sudoku.game.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.japskiddin.sudoku.core.domain.BoardDataSource
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameDataSource
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.domain.SettingsDataSource
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.data.BoardDataSourceImpl
import io.github.japskiddin.sudoku.data.BoardRepositoryImpl
import io.github.japskiddin.sudoku.data.SavedGameDataSourceImpl
import io.github.japskiddin.sudoku.data.SavedGameRepositoryImpl
import io.github.japskiddin.sudoku.data.SettingsDataSourceImpl
import io.github.japskiddin.sudoku.data.SettingsRepositoryImpl
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.datastore.SettingsDatastore

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideBoardDataSource(
        boardDao: BoardDao
    ): BoardDataSource = BoardDataSourceImpl(boardDao)

    @Provides
    fun provideSavedGameDataSource(
        savedGameDao: SavedGameDao
    ): SavedGameDataSource = SavedGameDataSourceImpl(savedGameDao)

    @Provides
    fun provideSettingsDataSource(
        datastore: SettingsDatastore
    ): SettingsDataSource = SettingsDataSourceImpl(datastore)

    @Provides
    fun provideBoardRepository(
        boardDataSource: BoardDataSource
    ): BoardRepository = BoardRepositoryImpl(boardDataSource)

    @Provides
    fun provideSavedGameRepository(
        savedGameDataSource: SavedGameDataSource
    ): SavedGameRepository = SavedGameRepositoryImpl(savedGameDataSource)

    @Provides
    fun provideSettingsRepository(
        settingsDataSource: SettingsDataSource
    ): SettingsRepository = SettingsRepositoryImpl(settingsDataSource)
}
