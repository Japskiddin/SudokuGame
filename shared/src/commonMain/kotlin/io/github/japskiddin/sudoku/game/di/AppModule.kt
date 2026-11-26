package io.github.japskiddin.sudoku.game.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.japskiddin.sudoku.core.common.AppDispatchers

@ContributesTo(AppScope::class)
@BindingContainer
public object AppModule {
    @SingleIn(AppScope::class)
    @Provides
    public fun provideAppDispatchers(): AppDispatchers = AppDispatchers()
}
