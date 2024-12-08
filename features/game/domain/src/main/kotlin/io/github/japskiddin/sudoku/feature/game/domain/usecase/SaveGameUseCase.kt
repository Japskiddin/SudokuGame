package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.core.model.isCurrent
import javax.inject.Inject

public class SaveGameUseCase
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
        time: Long,
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        val savedGame = savedGameRepository.get(uid)
        if (savedGame != null) {
            if (savedGame.isCurrent()) {
                savedGameRepository.update(
                    savedGame.copy(
                        board = board,
                        notes = notes,
                        actions = actions,
                        mistakes = mistakes,
                        time = time,
                        lastPlayed = currentTimeMillis,
                    )
                )
            }
        } else {
            savedGameRepository.insert(
                SavedGame(
                    uid = uid,
                    board = board,
                    notes = notes,
                    actions = actions,
                    mistakes = mistakes,
                    time = time,
                    lastPlayed = currentTimeMillis,
                    startedTime = currentTimeMillis,
                    finishedTime = 0L,
                    status = GameStatus.IN_PROGRESS,
                )
            )
        }
    }
}
