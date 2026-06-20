package io.github.japskiddin.sudoku.game.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.Logger
import io.github.japskiddin.sudoku.navigation.AppNavigator

@DependencyGraph(AppScope::class)
interface AppGraph {
    @Provides
    fun provideApplicationContext(application: Application): Context = application

    val logger: Logger
    val navigator: AppNavigator
    val appDispatchers: AppDispatchers

    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
