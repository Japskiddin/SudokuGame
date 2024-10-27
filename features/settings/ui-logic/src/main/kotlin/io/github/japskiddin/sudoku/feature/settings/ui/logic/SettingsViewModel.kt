package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
public class SettingsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState.Initial)

    public val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // TODO: Загружать из настроек
    }

    public fun onAction(action: UiAction) {
    }

    private fun tryNavigateToHome() = appNavigator.tryNavigateTo(Destination.HomeScreen())

    private suspend fun navigateToHome() = appNavigator.navigateTo(Destination.HomeScreen())
}
