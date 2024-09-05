package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.SavedGame
import javax.inject.Inject

public class GetSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public suspend operator fun invoke(uid: Long): SavedGame? = savedGameRepository.get(uid)
}