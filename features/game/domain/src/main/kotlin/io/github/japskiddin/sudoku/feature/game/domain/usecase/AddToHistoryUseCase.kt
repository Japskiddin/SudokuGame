package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.core.model.isCurrent
import javax.inject.Inject

public class AddToHistoryUseCase
@Inject
constructor(
    private val historyRepository: HistoryRepository,
    private val savedGameRepository: SavedGameRepository,
) {
    public suspend operator fun invoke(
        uid: Long,
        board: String,
        notes: String,
        actions: Int,
        mistakes: Int,
        time: Long,
        status: GameStatus,
    ): Long {
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
                        finishedTime = currentTimeMillis,
                        status = status,
                    )
                )
            }
        }

        val history = History(
            uid = uid,
            time = currentTimeMillis
        )
        return historyRepository.insert(history)
    }
}
