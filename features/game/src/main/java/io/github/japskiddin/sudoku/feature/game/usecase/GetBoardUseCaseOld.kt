package io.github.japskiddin.sudoku.feature.game.usecase

import io.github.japskiddin.sudoku.data.BoardRepository
import io.github.japskiddin.sudoku.data.model.GameLevel
import io.github.japskiddin.sudoku.feature.game.SudokuGenerator
import javax.inject.Inject

internal class GetBoardUseCaseOld @Inject constructor(
  private val repository: BoardRepository
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