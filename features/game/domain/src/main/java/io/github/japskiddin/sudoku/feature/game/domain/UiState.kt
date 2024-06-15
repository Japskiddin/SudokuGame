package io.github.japskiddin.sudoku.feature.game.domain

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.model.BoardNote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

public sealed class UiState {
    public class Loading(
        @StringRes public val message: Int
    ) : UiState()

    public class Error(
        @StringRes public val message: Int
    ) : UiState()

    public class Game(public val gameState: GameState) : UiState()

    public companion object {
        public val Initial: UiState = Loading(message = R.string.level_creation)
    }
}

public data class GameState(
    val board: ImmutableList<ImmutableList<BoardCell>>,
    val notes: ImmutableList<BoardNote>,
    val selectedCell: BoardCell
) {
    public companion object {
        public val Initial: GameState = GameState(
            board = List(9) { row ->
                List(9) { col ->
                    BoardCell(
                        row,
                        col,
                        0
                    )
                }.toImmutableList()
            }.toImmutableList(),
            notes = persistentListOf(),
            selectedCell = BoardCell(-1, -1)
        )
    }
}
