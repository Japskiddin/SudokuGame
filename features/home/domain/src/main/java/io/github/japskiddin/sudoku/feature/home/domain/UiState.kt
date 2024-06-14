package io.github.japskiddin.sudoku.feature.home.domain

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.data.model.SavedGame

public sealed class UiState {
    public class Loading(
        @StringRes public val message: Int
    ) : UiState()

    public data class Menu(
        @StringRes public val errorMessage: Int? = null,
        public val lastGame: SavedGame? = null
    ) : UiState()

    public companion object {
        public val Initial: UiState = Menu()
    }
}
