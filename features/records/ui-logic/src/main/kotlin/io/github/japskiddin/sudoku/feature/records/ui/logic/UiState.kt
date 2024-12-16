package io.github.japskiddin.sudoku.feature.records.ui.logic

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public data class UiState(
    val records: ImmutableList<RecordUI>,
) {
    public companion object {
        public val Initial: UiState = UiState(
            records = persistentListOf(),
        )
    }
}
