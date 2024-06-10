package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.data.SavedGameRepository
import javax.inject.Inject

internal class GetLastGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    operator fun invoke() = savedGameRepository.getLast()
}
