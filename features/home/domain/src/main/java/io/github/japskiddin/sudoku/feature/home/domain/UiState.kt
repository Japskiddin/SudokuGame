package io.github.japskiddin.sudoku.feature.home.domain

import androidx.annotation.StringRes

public sealed class UiState {
    public class Loading(
        @StringRes public val message: Int
    ) : UiState()

    public data class Menu(
        @StringRes public val errorMessage: Int? = null
    ) : UiState()

    public companion object {
        public val Initial: UiState = Menu()
    }
}
