package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

public class GetLastGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public operator fun invoke(): Flow<SavedGame?> =
        savedGameRepository.getLast().filter { it?.status == GameStatus.IN_PROGRESS }
}
