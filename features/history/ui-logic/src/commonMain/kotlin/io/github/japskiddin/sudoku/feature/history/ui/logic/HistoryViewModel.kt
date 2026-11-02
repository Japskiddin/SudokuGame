package io.github.japskiddin.sudoku.feature.history.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.japskiddin.sudoku.feature.history.domain.usecase.GetHistoryUseCase
import io.github.japskiddin.sudoku.feature.history.ui.logic.utils.toHistoryUI
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
public class HistoryViewModel(
    private val appNavigator: AppNavigator,
    getHistoryUseCase: GetHistoryUseCase,
) : ViewModel() {
    public val uiState: StateFlow<UiState> = getHistoryUseCase.invoke()
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
