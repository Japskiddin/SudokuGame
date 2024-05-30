package io.github.japskiddin.sudoku.feature.game.domain

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import io.github.japskiddin.sudoku.core.game.model.BoardCell

@Stable
public sealed class UiState {
  @Stable
  public class Loading(@StringRes public val message: Int) : UiState()

  @Stable
  public class Error(@StringRes public val message: Int) : UiState()

  @Stable
  public class Success(public val gameState: GameState) : UiState()

  public companion object {
    public val Initial: UiState = Loading(message = R.string.level_creation)
  }
}

@Immutable
public data class GameState(
  val board: List<List<BoardCell>>,
  val selectedCell: BoardCell = BoardCell(-1, -1, 0),
)