package io.github.japskiddin.sudoku.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.game.ui.navigation.AppNavigator
import io.github.japskiddin.sudoku.game.ui.navigation.AppNavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigatorImpl()
}