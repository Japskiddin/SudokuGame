package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.GetAppPreferencesUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveMistakesLimitPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveShowTimerPreferenceUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
public class SettingsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val appDispatchers: AppDispatchers,
    getAppPreferencesUseCase: Provider<GetAppPreferencesUseCase>,
    private val saveMistakesLimitPreferenceUseCase: Provider<SaveMistakesLimitPreferenceUseCase>,
    private val saveShowTimerPreferenceUseCase: Provider<SaveShowTimerPreferenceUseCase>
) : ViewModel() {
    public val uiState: StateFlow<UiState> = getAppPreferencesUseCase.get().invoke().map { preferences ->
        UiState(
            isMistakesLimit = preferences.isMistakesLimit,
            isShowTimer = preferences.isShowTimer
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState.Initial
    )

    public fun onAction(action: UiAction) {
        when (action) {
            is UiAction.UpdateMistakesLimit -> updateMistakesLimit(action.checked)
            is UiAction.UpdateShowTimer -> updateShowTimer(action.checked)
        }
    }

    private fun updateShowTimer(enabled: Boolean) {
        viewModelScope.launch(appDispatchers.io) {
            saveShowTimerPreferenceUseCase.get().invoke(enabled)
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
