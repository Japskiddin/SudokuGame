package io.github.japskiddin.sudoku.feature.history.ui.logic

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public data class UiState(
    val history: ImmutableList<HistoryUI>,
) {
    public companion object {
        public val Initial: UiState = UiState(
            history = persistentListOf(),
        )
    }
}
