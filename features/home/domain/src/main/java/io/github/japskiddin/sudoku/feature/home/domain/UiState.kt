package io.github.japskiddin.sudoku.feature.home.domain

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
public sealed class UiState {
  @Stable
  public class Loading(@StringRes public val message: Int) : UiState()

  @Immutable
  public data object Menu : UiState()

  public companion object {
    public val Initial: UiState = Menu
  }
}