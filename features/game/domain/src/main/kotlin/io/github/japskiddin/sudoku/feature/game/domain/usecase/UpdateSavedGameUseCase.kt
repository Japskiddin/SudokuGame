package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.SavedGame
import javax.inject.Inject

public class UpdateSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public suspend operator fun invoke(
        savedGame: SavedGame,
        timer: Long,
        board: String,
        notes: String,
        mistakes: Int,
        lastPlayed: Long
    ) {
        savedGameRepository.update(
            savedGame.copy(
                timer = timer,
                board = board,
                notes = notes,
                mistakes = mistakes,
                lastPlayed = lastPlayed
            )
        )
    }
}
