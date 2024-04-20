package io.github.japskiddin.sudoku.feature.main.usecase

import io.github.japskiddin.sudoku.data.GameRepository
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.SudokuGenerator
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
        return GameLevel(board = result.items, completedBoard = result.completedItems)
    }
}