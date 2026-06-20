package io.github.japskiddin.sudoku.game.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.Logger
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.navigation.AppNavigator

@DependencyGraph(AppScope::class)
interface AppGraph {
    @Provides
    fun provideApplicationContext(application: Application): Context = application

    val database: SudokuDatabase
    val historyDao: HistoryDao
    val settingsDatastore: SettingsDatastore
    val logger: Logger
    val navigator: AppNavigator
    val appDispatchers: AppDispatchers

    @SingleIn(AppScope::class)
    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettingsDatastore(application: Context): SettingsDatastore = SettingsDatastore(application)

    @SingleIn(AppScope::class)
    @Provides
    fun provideDatabase(application: Context): SudokuDatabase = SudokuDatabase(application)

    @Provides
    fun provideHistoryDay(database: SudokuDatabase): HistoryDao = database.historyDao

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
