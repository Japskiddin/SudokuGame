package io.github.japskiddin.sudoku.feature.settings.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.GetAppPreferencesUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveGameModePreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveHighlightErrorCellsPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveHighlightSelectedCellPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveHighlightSimilarCellsPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveKeepScreenOnPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveMistakesLimitPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveResetTimerPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveShowRemainingNumbersPreferenceUseCase
import io.github.japskiddin.sudoku.feature.settings.domain.usecase.SaveShowTimerPreferenceUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
public class SettingsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
    getAppPreferencesUseCase: Provider<GetAppPreferencesUseCase>,
    private val saveMistakesLimitPreferenceUseCase: Provider<SaveMistakesLimitPreferenceUseCase>,
    private val saveShowTimerPreferenceUseCase: Provider<SaveShowTimerPreferenceUseCase>,
    private val saveResetTimerPreferenceUseCase: Provider<SaveResetTimerPreferenceUseCase>,
    private val saveHighlightErrorCellsPreferenceUseCase: Provider<SaveHighlightErrorCellsPreferenceUseCase>,
    private val saveHighlightSimilarCellsPreferenceUseCase: Provider<SaveHighlightSimilarCellsPreferenceUseCase>,
    private val saveHighlightSelectedCellPreferenceUseCase: Provider<SaveHighlightSelectedCellPreferenceUseCase>,
    private val saveKeepScreenOnPreferenceUseCase: Provider<SaveKeepScreenOnPreferenceUseCase>,
    private val saveGameModePreferenceUseCase: Provider<SaveGameModePreferenceUseCase>,
    private val saveShowRemainingNumbersPreferenceUseCase: Provider<SaveShowRemainingNumbersPreferenceUseCase>,
) : ViewModel() {
    public val uiState: StateFlow<UiState> = getAppPreferencesUseCase.get().invoke().map { preferences ->
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
            saveShowRemainingNumbersPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateSaveGameMode(enabled: Boolean) {
        viewModelScope.launch {
            saveGameModePreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateKeepScreenOn(enabled: Boolean) {
        viewModelScope.launch {
            saveKeepScreenOnPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateHighlightSelectedCell(enabled: Boolean) {
        viewModelScope.launch {
            saveHighlightSelectedCellPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateHighlightSimilarCells(enabled: Boolean) {
        viewModelScope.launch {
            saveHighlightSimilarCellsPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateHighlightErrorCells(enabled: Boolean) {
        viewModelScope.launch {
            saveHighlightErrorCellsPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateShowTimer(enabled: Boolean) {
        viewModelScope.launch {
            saveShowTimerPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateMistakesLimit(enabled: Boolean) {
        viewModelScope.launch {
            saveMistakesLimitPreferenceUseCase.get().invoke(enabled)
        }
    }

    private fun updateResetTimer(enabled: Boolean) {
        viewModelScope.launch {
            saveResetTimerPreferenceUseCase.get().invoke(enabled)
        }
    }
}
