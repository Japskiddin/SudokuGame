package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.GetMistakesLimitUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveMistakesLimitUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class SettingsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    private val getMistakesLimitUseCase: Provider<GetMistakesLimitUseCase>,
    private val saveMistakesLimitUseCase: Provider<SaveMistakesLimitUseCase>
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState.Initial)

    public val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(appDispatchers.io) {
            getMistakesLimitUseCase.get().invoke().collect { isMistakesLimit ->
                _uiState.update { it.copy(isMistakesLimit = isMistakesLimit) }
            }
        }
    }

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.UpdateMistakesLimit -> updateMistakesLimit(action.checked)
        }
    }

    private fun updateMistakesLimit(enabled: Boolean) {
        viewModelScope.launch(appDispatchers.io) {
            saveMistakesLimitUseCase.get().invoke(enabled)
        }
    }

    private fun tryNavigateToHome() = appNavigator.tryNavigateTo(Destination.HomeScreen())

    private suspend fun navigateToHome() = appNavigator.navigateTo(Destination.HomeScreen())
}
