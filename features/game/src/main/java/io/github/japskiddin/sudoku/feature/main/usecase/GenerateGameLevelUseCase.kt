package io.github.japskiddin.sudoku.feature.main.usecase

import io.github.japskiddin.sudoku.data.GameRepository
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.utils.generateGameBoard
import javax.inject.Inject

internal class GenerateGameLevelUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): GameLevel {
//        return repository.getGameLevel()
        val board = generateGameBoard()
        return GameLevel(board = board)
    }
}