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
        val generator = SudokuGenerator(9, 40)
        val board = generator.fillValues()
        return GameLevel(board = board)
    }
}