package io.github.japskiddin.sudoku.feature.history.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.common.android.di.ViewModelKey
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
public class HistoryViewModel
internal constructor(
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

    public companion object {
        public fun factory(
            appNavigator: AppNavigator,
            getHistoryUseCase: GetHistoryUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HistoryViewModel(
                    appNavigator = appNavigator,
                    getHistoryUseCase = getHistoryUseCase,
                )
            }
        }
    }
}
