package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.common.AppDispatchers
import io.github.japskiddin.sudoku.database.SudokuDatabase
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