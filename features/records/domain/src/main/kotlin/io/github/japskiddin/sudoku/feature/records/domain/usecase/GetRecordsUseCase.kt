package io.github.japskiddin.sudoku.feature.records.domain.usecase

import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.RecordRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.Record
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

public class GetRecordsUseCase
@Inject
constructor(
    private val recordRepository: RecordRepository,
    private val savedGameRepository: SavedGameRepository,
    private val boardRepository: BoardRepository,
) {
    public operator fun invoke(): Flow<List<CombinedRecord>> = flow {
        recordRepository.getAll().collect { records ->
            val result: MutableList<CombinedRecord> = mutableListOf()

            records.forEach { record ->
                val boardUid = record.uid
                val board = boardRepository.get(boardUid)
                val savedGame = savedGameRepository.get(boardUid)
                if (board != null && savedGame != null) {
                    result.add(
                        CombinedRecord(
                            record = record,
                            savedGame = savedGame,
                            board = board,
                        )
                    )
                }
            }

            result.sortBy { combinedRecord ->
                combinedRecord.record.time
            }
            emit(result)
        }
    }
}

public data class CombinedRecord(
    val record: Record,
    val savedGame: SavedGame,
    val board: Board,
)
