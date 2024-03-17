package io.github.japskiddin.sudoku.game

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    @Provides
//    @Singleton
//    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase
}