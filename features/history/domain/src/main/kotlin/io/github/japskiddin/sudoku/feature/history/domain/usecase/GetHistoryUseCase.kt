package io.github.japskiddin.sudoku.feature.history.domain.usecase

import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

public class GetHistoryUseCase
@Inject
constructor(
    private val historyRepository: HistoryRepository,
    private val savedGameRepository: SavedGameRepository,
    private val boardRepository: BoardRepository,
) {
    public operator fun invoke(): Flow<List<CombinedHistory>> = flow {
        historyRepository.getAll().collect { histories ->
            val result: MutableList<CombinedHistory> = mutableListOf()

            histories.forEach { history ->
                val boardUid = history.uid
                val board = boardRepository.get(boardUid)
                val savedGame = savedGameRepository.get(boardUid)
                if (board != null && savedGame != null) {
                    result.add(
                        CombinedHistory(
                            history = history,
                            savedGame = savedGame,
                            board = board,
                        )
                    )
                }
            }

            result.sortBy { combinedHistory ->
                combinedHistory.history.time
            }
            emit(result)
        }
    }
}

public data class CombinedHistory(
    val history: History,
    val savedGame: SavedGame,
    val board: Board,
)
