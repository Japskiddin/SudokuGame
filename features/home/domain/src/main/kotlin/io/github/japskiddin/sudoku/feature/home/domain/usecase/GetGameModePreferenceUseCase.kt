package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.GameMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

public class GetGameModePreferenceUseCase
@Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    public operator fun invoke(): Flow<GameMode> {
        return combine(
            settingsRepository.getGameMode(),
            settingsRepository.isSaveGameMode()
        ) { gameMode, isSave ->
            if (isSave) {
                gameMode
            } else {
                GameMode.Initial
            }
        }
    }
}
