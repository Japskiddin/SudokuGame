package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.datastore.SettingsDatastore

@BindingContainer
object AppModule {
    @SingleIn(AppScope::class)
    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettingsDatastore(application: Context): SettingsDatastore = SettingsDatastore(application)
}
