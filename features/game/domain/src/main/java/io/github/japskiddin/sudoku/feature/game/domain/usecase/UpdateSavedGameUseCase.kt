package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.data.SavedGameRepository
import io.github.japskiddin.sudoku.data.model.SavedGame
import javax.inject.Inject

internal class UpdateSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    suspend operator fun invoke(
        savedGame: SavedGame,
        timer: Long,
        currentBoard: String,
        notes: String,
        mistakes: Int,
        lastPlayed: Long
    ) {
        savedGameRepository.update(
            savedGame.copy(
                timer = timer,
                currentBoard = currentBoard,
                notes = notes,
                mistakes = mistakes,
                lastPlayed = lastPlayed
            )
        )
    }
}
