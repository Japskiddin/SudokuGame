package io.github.japskiddin.sudoku.game.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.Logger
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.navigation.AppNavigator

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph, ActivityGraph.Factory {
    @Provides
    fun provideContext(application: Application): Context = application

    val logger: Logger
    val navigator: AppNavigator
    val appDispatchers: AppDispatchers
    val settingsRepository: SettingsRepository
    val boardRepository: BoardRepository
    val historyRepository: HistoryRepository
    val savedGameRepository: SavedGameRepository

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
