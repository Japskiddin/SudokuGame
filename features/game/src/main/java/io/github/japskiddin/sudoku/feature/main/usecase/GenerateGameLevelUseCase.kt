package io.github.japskiddin.sudoku.feature.main.usecase

import io.github.japskiddin.sudoku.data.GameRepository
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.utils.generateGameBoard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GenerateGameLevelUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): Flow<GameLevel> {
//        return repository.getGameLevel()
        return flow {
            val board = generateGameBoard()
            val gameLevel = GameLevel(board = board)
            emit(gameLevel)
        }
    }
}