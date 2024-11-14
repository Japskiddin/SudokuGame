package io.github.japskiddin.sudoku.feature.settings.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.AppPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class GetAppPreferencesUseCase
@Inject
constructor(
    private val settingsRepository: SettingsRepository
) {
    public operator fun invoke(): Flow<AppPreferences> = settingsRepository.getAppPreferences()
}
