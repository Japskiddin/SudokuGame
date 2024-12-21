package io.github.japskiddin.sudoku.feature.history.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.feature.history.domain.usecase.GetHistoryUseCase
import io.github.japskiddin.sudoku.feature.history.ui.logic.utils.toHistoryUI
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class HistoryViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    getHistoryUseCase: Provider<GetHistoryUseCase>,
) : ViewModel() {
    public val uiState: StateFlow<UiState> = getHistoryUseCase.get().invoke()
        .map {
            UiState(
                history = it
                    .map { combinedHistory ->
                        combinedHistory.toHistoryUI()
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
