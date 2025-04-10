package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
public class SettingsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    public val uiState: StateFlow<UiState> = settingsRepository.getAppPreferences().map { preferences ->
        UiState(
            isMistakesLimit = preferences.isMistakesLimit,
            isShowTimer = preferences.isShowTimer,
            isResetTimer = preferences.isResetTimer,
            isHighlightErrorCells = preferences.isHighlightErrorCells,
            isHighlightSimilarCells = preferences.isHighlightSimilarCells,
            isHighlightSelectedCell = preferences.isHighlightSelectedCell,
            isKeepScreenOn = preferences.isKeepScreenOn,
            isSaveGameMode = preferences.isSaveGameMode,
            isShowRemainingNumbers = preferences.isShowRemainingNumbers,
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
            is UiAction.UpdateResetTimer -> updateResetTimer(action.checked)
            is UiAction.UpdateHighlightErrorCells -> updateHighlightErrorCells(action.checked)
            is UiAction.UpdateHighlightSimilarCells -> updateHighlightSimilarCells(action.checked)
            is UiAction.UpdateHighlightSelectedCell -> updateHighlightSelectedCell(action.checked)
            is UiAction.UpdateKeepScreenOn -> updateKeepScreenOn(action.checked)
            is UiAction.UpdateSaveGameMode -> updateSaveGameMode(action.checked)
            is UiAction.UpdateShowRemainingNumbers -> updateShowRemainingNumbers(action.checked)
            is UiAction.Back -> appNavigator.tryNavigateBack()
        }
    }

    private fun updateShowRemainingNumbers(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowRemainingNumbers(enabled)
        }
    }

    private fun updateSaveGameMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSaveGameMode(enabled)
        }
    }

    private fun updateKeepScreenOn(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setKeepScreenOn(enabled)
        }
    }

    private fun updateHighlightSelectedCell(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighlightSelectedCell(enabled)
        }
    }

    private fun updateHighlightSimilarCells(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighlightSimilarCells(enabled)
        }
    }

    private fun updateHighlightErrorCells(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighlightErrorCells(enabled)
        }
    }

    private fun updateShowTimer(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowTimer(enabled)
        }
    }

    private fun updateMistakesLimit(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setMistakesLimit(enabled)
        }
    }

    private fun updateResetTimer(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setResetTimer(enabled)
        }
    }
}
