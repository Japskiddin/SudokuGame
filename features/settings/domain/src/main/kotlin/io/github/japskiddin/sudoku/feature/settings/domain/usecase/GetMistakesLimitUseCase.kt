package io.github.japskiddin.sudoku.feature.settings.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class GetMistakesLimitUseCase
@Inject
constructor(private val settingsRepository: SettingsRepository) {
    public operator fun invoke(): Flow<Boolean> = settingsRepository.isMistakesLimit()
}
