package io.github.japskiddin.sudoku.game.di

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.feature.game.domain.usecase.AddToHistoryUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.CheckGameStatusUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.GetBoardUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.RestoreGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.SaveGameUseCase
import io.github.japskiddin.sudoku.feature.game.domain.usecase.SolveBoardUseCase
import io.github.japskiddin.sudoku.feature.history.domain.usecase.GetHistoryUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.DeleteSavedGameUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GenerateSudokuUseCase
import io.github.japskiddin.sudoku.feature.home.domain.usecase.GetGameModePreferenceUseCase

@BindingContainer
object UseCaseModule {
    @Provides
    fun provideGenerateSudokuUseCase(appDispatchers: AppDispatchers): GenerateSudokuUseCase =
        GenerateSudokuUseCase(appDispatchers)

    @Provides
    fun provideGetGameModePreferenceUseCase(settingsRepository: SettingsRepository): GetGameModePreferenceUseCase =
        GetGameModePreferenceUseCase(settingsRepository)

    @Provides
    fun provideDeleteSavedGameUseCase(savedGameRepository: SavedGameRepository): DeleteSavedGameUseCase =
        DeleteSavedGameUseCase(savedGameRepository)

    @Provides
    fun provideGetBoardUseCase(boardRepository: BoardRepository): GetBoardUseCase =
        GetBoardUseCase(boardRepository)

    @Provides
    fun provideSaveGameUseCase(savedGameRepository: SavedGameRepository): SaveGameUseCase =
        SaveGameUseCase(savedGameRepository)

    @Provides
    fun provideRestoreGameUseCase(appDispatchers: AppDispatchers): RestoreGameUseCase =
        RestoreGameUseCase(appDispatchers)

    @Provides
    fun provideSolveBoardUseCase(appDispatchers: AppDispatchers): SolveBoardUseCase =
        SolveBoardUseCase(appDispatchers)

    @Provides
    fun provideAddToHistoryUseCase(
        historyRepository: HistoryRepository,
        savedGameRepository: SavedGameRepository
    ): AddToHistoryUseCase = AddToHistoryUseCase(historyRepository, savedGameRepository)

    @Provides
    fun provideGetHistoryUseCase(
        historyRepository: HistoryRepository,
        savedGameRepository: SavedGameRepository,
        boardRepository: BoardRepository
    ): GetHistoryUseCase = GetHistoryUseCase(historyRepository, savedGameRepository, boardRepository)

    @Provides
    fun provideCheckGameStatusUseCase(appDispatchers: AppDispatchers): CheckGameStatusUseCase =
        CheckGameStatusUseCase(appDispatchers)
}
