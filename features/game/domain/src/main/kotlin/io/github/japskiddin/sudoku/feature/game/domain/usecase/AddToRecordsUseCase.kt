package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.RecordRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.Record
import io.github.japskiddin.sudoku.core.model.isCurrent
import javax.inject.Inject

public class AddToRecordsUseCase
@Inject
constructor(
    private val recordRepository: RecordRepository,
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

        val record = Record(
            uid = uid,
            time = currentTimeMillis
        )
        return recordRepository.insert(record)
    }
}
