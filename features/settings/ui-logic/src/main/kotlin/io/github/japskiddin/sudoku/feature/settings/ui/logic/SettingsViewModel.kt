package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.GetMistakesLimitPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.GetTimerPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveMistakesLimitPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveTimerPreferenceUseCase
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
    private val getMistakesLimitPreferenceUseCase: Provider<GetMistakesLimitPreferenceUseCase>,
    private val saveMistakesLimitPreferenceUseCase: Provider<SaveMistakesLimitPreferenceUseCase>,
    private val getTimerPreferenceUseCase: Provider<GetTimerPreferenceUseCase>,
    private val saveTimerPreferenceUseCase: Provider<SaveTimerPreferenceUseCase>
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState.Initial)

    public val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(appDispatchers.io) {
            getMistakesLimitPreferenceUseCase.get().invoke().collect { isMistakesLimit ->
                _uiState.update { it.copy(isMistakesLimit = isMistakesLimit) }
            }
            getTimerPreferenceUseCase.get().invoke().collect { isTimer ->
                _uiState.update { it.copy(isTimer = isTimer) }
            }
        }
    }

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.UpdateMistakesLimit -> updateMistakesLimit(action.checked)
            is UiAction.UpdateTimer -> updateTimer(action.checked)
        }
    }

    private fun updateTimer(enabled: Boolean) {
        viewModelScope.launch(appDispatchers.io) {
            saveTimerPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateMistakesLimit(enabled: Boolean) {
        viewModelScope.launch(appDispatchers.io) {
            saveMistakesLimitPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun tryNavigateToHome() = appNavigator.tryNavigateTo(Destination.HomeScreen())

    private suspend fun navigateToHome() = appNavigator.navigateTo(Destination.HomeScreen())
}
