package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.GameMode
import javax.inject.Inject

public class SetGameModePreferenceUseCase
@Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    public suspend operator fun invoke(gameMode: GameMode) {
        settingsRepository.setGameMode(gameMode)
    }
}
