package io.github.japskiddin.sudoku.feature.home.domain

public sealed class UiState {
    public data object Loading : UiState()

    public data class Error(
        public val code: ErrorCode = ErrorCode.NONE
    ) : UiState()

    public data class Menu(
        public val isContinueVisible: Boolean = false
    ) : UiState()

    public companion object {
        public val Initial: UiState = Menu()
    }
}
