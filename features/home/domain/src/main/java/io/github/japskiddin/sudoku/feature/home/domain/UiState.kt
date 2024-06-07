package io.github.japskiddin.sudoku.feature.home.domain

import androidx.annotation.StringRes

public sealed class UiState {
    public class Loading(@StringRes public val message: Int) : UiState()

    public data object Menu : UiState()

    public companion object {
        public val Initial: UiState = Menu
    }
}