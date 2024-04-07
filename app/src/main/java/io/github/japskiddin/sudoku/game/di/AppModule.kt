package io.github.japskiddin.sudoku.game.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.japskiddin.sudoku.common.AppDispatchers
import io.github.japskiddin.sudoku.database.GameDatabase
import io.github.japskiddin.sudoku.datastore.GameDatastore
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.AppNavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return GameDatabase(context)
    }

    @Provides
    @Singleton
    fun provideGameDatastore(@ApplicationContext context: Context): GameDatastore {
        return GameDatastore(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppNavigatorImpl()
}