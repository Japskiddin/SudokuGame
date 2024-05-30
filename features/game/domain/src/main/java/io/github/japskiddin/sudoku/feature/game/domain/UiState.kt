package io.github.japskiddin.sudoku.feature.game.domain

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import kotlinx.collections.immutable.ImmutableList

public sealed class UiState {
  public class Loading(@StringRes public val message: Int) : UiState()

  public class Error(@StringRes public val message: Int) : UiState()

  public class Success(public val gameState: GameState) : UiState()

  public companion object {
    public val Initial: UiState = Loading(message = R.string.level_creation)
  }
}

public data class GameState(
  val board: ImmutableList<ImmutableList<BoardCell>>,
  val selectedCell: BoardCell = BoardCell(-1, -1, 0),
)