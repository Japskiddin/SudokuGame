package io.github.japskiddin.sudoku.feature.game.usecase

import io.github.japskiddin.sudoku.data.SavedGameRepository
import jakarta.inject.Inject

internal class GetSavedGameUseCase @Inject constructor(
  private val savedGameRepository: SavedGameRepository
) {
  suspend operator fun invoke(uid: Long) = savedGameRepository.get(uid)
}