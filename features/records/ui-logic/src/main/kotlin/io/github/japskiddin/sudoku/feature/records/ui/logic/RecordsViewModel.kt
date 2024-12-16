package io.github.japskiddin.sudoku.feature.records.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.feature.records.domain.usecase.GetRecordsUseCase
import io.github.japskiddin.sudoku.feature.records.ui.logic.utils.toRecordUI
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class RecordsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    getRecordsUseCase: Provider<GetRecordsUseCase>,
) : ViewModel() {
    public val uiState: StateFlow<UiState> = getRecordsUseCase.get().invoke()
        .map {
            UiState(
                records = it
                    .map { combinedRecord ->
                        combinedRecord.toRecordUI()
                    }
                    .toImmutableList()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Initial
        )

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.Back -> onBackPressed()
        }
    }

    private fun onBackPressed() = appNavigator.tryNavigateBack()
}
