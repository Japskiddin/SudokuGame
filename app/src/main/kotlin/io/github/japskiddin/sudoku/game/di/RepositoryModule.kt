package io.github.japskiddin.sudoku.game.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.data.BoardRepositoryImpl
import io.github.japskiddin.sudoku.data.SavedGameRepositoryImpl
import io.github.japskiddin.sudoku.data.SettingsRepositoryImpl
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.datastore.SettingsDatastore

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideBoardRepository(
        boardDao: BoardDao
    ): BoardRepository = BoardRepositoryImpl(boardDao)

    @Provides
    fun provideSavedGameRepository(
        savedGameDao: SavedGameDao
    ): SavedGameRepository = SavedGameRepositoryImpl(savedGameDao)

    @Provides
    fun provideSettingsRepository(
        datastore: SettingsDatastore
    ): SettingsRepository = SettingsRepositoryImpl(datastore)
}
