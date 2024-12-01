package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.GameMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class GetLastGameModePreferenceUseCase
@Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    public operator fun invoke(): Flow<GameMode> = settingsRepository.getLastGameMode()
}
