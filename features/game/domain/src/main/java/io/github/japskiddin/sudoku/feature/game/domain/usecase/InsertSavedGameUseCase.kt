package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.game.GameStatus
import io.github.japskiddin.sudoku.data.SavedGameRepository
import io.github.japskiddin.sudoku.data.model.SavedGame
import javax.inject.Inject

internal class InsertSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    suspend operator fun invoke(
        uid: Long,
        currentBoard: String,
        notes: String,
        actions: Int,
        mistakes: Int,
        timer: Long,
        lastPlayed: Long,
        startedAt: Long,
        finishedAt: Long,
        status: GameStatus
    ) {
        savedGameRepository.insert(
            SavedGame(
                uid = uid,
                currentBoard = currentBoard,
                notes = notes,
                actions = actions,
                mistakes = mistakes,
                timer = timer,
                lastPlayed = lastPlayed,
                startedAt = startedAt,
                finishedAt = finishedAt,
                status = status
            )
        )
    }
}
