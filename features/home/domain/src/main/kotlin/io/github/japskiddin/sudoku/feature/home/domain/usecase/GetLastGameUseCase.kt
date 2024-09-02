package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.data.SavedGameRepository
import io.github.japskiddin.sudoku.data.model.SavedGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class GetLastGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public operator fun invoke(): Flow<SavedGame?> = savedGameRepository.getLast()
}
