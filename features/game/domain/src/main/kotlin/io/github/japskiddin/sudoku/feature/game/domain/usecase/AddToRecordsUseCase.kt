package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.domain.RecordRepository
import io.github.japskiddin.sudoku.core.model.Record
import javax.inject.Inject

public class AddToRecordsUseCase
@Inject
constructor(
    private val recordRepository: RecordRepository
) {
    public suspend operator fun invoke(uid: Long): Long {
        val record = Record(
            uid = uid,
            time = System.currentTimeMillis()
        )
        return recordRepository.insert(record)
    }
}
