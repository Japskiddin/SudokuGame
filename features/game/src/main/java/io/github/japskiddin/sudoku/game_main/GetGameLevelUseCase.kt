package io.github.japskiddin.sudoku.game_main

import io.github.japskiddin.sudoku.game_data.GameRepository
import io.github.japskiddin.sudoku.game_data.models.GameLevel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameLevelUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): Flow<GameLevel> {
        return repository.getGameLevel()
    }
}