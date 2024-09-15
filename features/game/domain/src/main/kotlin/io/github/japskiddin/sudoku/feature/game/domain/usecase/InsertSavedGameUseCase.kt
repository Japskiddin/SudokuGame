package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.SavedGame
import javax.inject.Inject

public class InsertSavedGameUseCase
@Inject
constructor(
    private val savedGameRepository: SavedGameRepository
) {
    public suspend operator fun invoke(
        uid: Long,
        board: String,
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
                board = board,
                notes = notes,
                actions = actions,
                mistakes = mistakes,
                timer = timer,
                lastPlayed = lastPlayed,
                startedTime = startedAt,
                finishedTime = finishedAt,
                status = status
            )
        )
    }
}
