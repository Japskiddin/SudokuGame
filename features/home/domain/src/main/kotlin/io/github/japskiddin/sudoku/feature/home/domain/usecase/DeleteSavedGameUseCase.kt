package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.core.model.isCurrent
import javax.inject.Inject

public class DeleteSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public suspend operator fun invoke(savedGame: SavedGame) {
        if (savedGame.isCurrent()) {
            savedGameRepository.delete(savedGame)
        }
    }
}
