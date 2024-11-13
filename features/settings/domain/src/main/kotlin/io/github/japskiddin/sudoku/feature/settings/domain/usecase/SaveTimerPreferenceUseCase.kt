package io.github.japskiddin.sudoku.feature.settings.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import javax.inject.Inject

public class SaveTimerPreferenceUseCase
@Inject
constructor(
    private val settingsRepository: SettingsRepository
) {
    public suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.setTimer(enabled)
    }
}
