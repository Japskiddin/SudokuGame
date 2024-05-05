package io.github.japskiddin.sudoku.feature.game.usecase

import io.github.japskiddin.sudoku.data.GameRepository
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.game.SudokuGenerator
import javax.inject.Inject

internal class GenerateGameLevelUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): GameLevel {
//        return repository.getGameLevel()
        val generator = SudokuGenerator(9, 30).apply {
            generate()
        }
        val result = generator.getResult()
        return GameLevel(
            defaultBoard = result.items,
            currentBoard = result.items,
            completedBoard = result.completedItems
        )
    }
}