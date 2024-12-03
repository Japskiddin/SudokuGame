package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.japskiddin.sudoku.datastore.model.AppPreferencesDSO
import io.github.japskiddin.sudoku.datastore.model.GameModeDSO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

public class SettingsDatastore(applicationContext: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = applicationContext.createDataStore

    private val mistakesLimitKey = booleanPreferencesKey(KEY_MISTAKES_LIMIT)
    private val timerKey = booleanPreferencesKey(KEY_SHOW_TIMER)
    private val resetTimerKey = booleanPreferencesKey(KEY_RESET_TIMER)
    private val highlightErrorCellsKey = booleanPreferencesKey(KEY_HIGHLIGHT_ERROR_CELLS)
    private val highlightSimilarCellsKey = booleanPreferencesKey(KEY_HIGHLIGHT_SIMILAR_CELLS)
    private val showRemainingNumbersKey = booleanPreferencesKey(KEY_SHOW_REMAINING_NUMBERS)
    private val highlightSelectedCellKey = booleanPreferencesKey(KEY_HIGHLIGHT_SELECTED_CELL)
    private val keepScreenOnKey = booleanPreferencesKey(KEY_KEEP_SCREEN_ON)
    private val saveGameModeKey = booleanPreferencesKey(KEY_SAVE_GAME_MODE)
    private val gameModeDifficultyKey = intPreferencesKey(KEY_GAME_MODE_DIFFICULTY)
    private val gameModeTypeKey = intPreferencesKey(KEY_GAME_MODE_TYPE)

    public suspend fun setMistakesLimit(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[mistakesLimitKey] = enabled
        }
    }

    public val isMistakesLimit: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[mistakesLimitKey] ?: DEFAULT_MISTAKES_LIMIT
    }

    public suspend fun setShowTimer(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[timerKey] = enabled
        }
    }

    public val isShowTimer: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[timerKey] ?: DEFAULT_SHOW_TIMER
    }

    public suspend fun setResetTimer(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[resetTimerKey] = enabled
        }
    }

    public val isResetTimer: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[resetTimerKey] ?: DEFAULT_RESET_TIMER
    }

    public suspend fun setHighlightErrorCells(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[highlightErrorCellsKey] = enabled
        }
    }

    public val isHighlightErrorCells: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[highlightErrorCellsKey] ?: DEFAULT_HIGHLIGHT_ERROR_CELLS
    }

    public suspend fun setHighlightSimilarCells(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[highlightSimilarCellsKey] = enabled
        }
    }

    public val isHighlightSimilarCells: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[highlightSimilarCellsKey] ?: DEFAULT_HIGHLIGHT_SIMILAR_CELLS
    }

    public suspend fun setShowRemainingNumbers(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[showRemainingNumbersKey] = enabled
        }
    }

    public val isShowRemainingNumbers: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[showRemainingNumbersKey] ?: DEFAULT_SHOW_REMAINING_NUMBERS
    }

    public suspend fun setHighlightSelectedCell(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[highlightSelectedCellKey] = enabled
        }
    }

    public val isHighlightSelectedCell: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[highlightSelectedCellKey] ?: DEFAULT_HIGHLIGHT_SELECTED_CELL
    }

    public suspend fun setKeepScreenOn(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[keepScreenOnKey] = enabled
        }
    }

    public val isKeepScreenOn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[keepScreenOnKey] ?: DEFAULT_KEEP_SCREEN_ON
    }

    public suspend fun setSaveGameMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[saveGameModeKey] = enabled
        }
    }

    public val isSaveGameMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[saveGameModeKey] ?: DEFAULT_SAVE_GAME_MODE
    }

    public suspend fun setGameMode(mode: GameModeDSO) {
        dataStore.edit { preferences ->
            preferences[gameModeDifficultyKey] = mode.difficulty
            preferences[gameModeTypeKey] = mode.type
        }
    }

    public val gameMode: Flow<GameModeDSO?> = dataStore.data.map { preferences ->
        val isSaveLastGameMode = preferences[saveGameModeKey] ?: DEFAULT_SAVE_GAME_MODE

        if (isSaveLastGameMode) {
            GameModeDSO(
                difficulty = preferences[gameModeDifficultyKey] ?: DEFAULT_GAME_MODE_DIFFICULTY,
                type = preferences[gameModeTypeKey] ?: DEFAULT_GAME_MODE_TYPE,
            )
        } else {
            null
        }
    }

    public val appPreferences: Flow<AppPreferencesDSO> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            AppPreferencesDSO(
                isMistakesLimit = preferences[mistakesLimitKey] ?: DEFAULT_MISTAKES_LIMIT,
                isShowTimer = preferences[timerKey] ?: DEFAULT_SHOW_TIMER,
                isResetTimer = preferences[resetTimerKey] ?: DEFAULT_RESET_TIMER,
                isHighlightErrorCells = preferences[highlightErrorCellsKey] ?: DEFAULT_HIGHLIGHT_ERROR_CELLS,
                isHighlightSimilarCells = preferences[highlightSimilarCellsKey] ?: DEFAULT_HIGHLIGHT_SIMILAR_CELLS,
                isShowRemainingNumbers = preferences[showRemainingNumbersKey] ?: DEFAULT_SHOW_REMAINING_NUMBERS,
                isHighlightSelectedCell = preferences[highlightSelectedCellKey] ?: DEFAULT_HIGHLIGHT_SELECTED_CELL,
                isKeepScreenOn = preferences[keepScreenOnKey] ?: DEFAULT_KEEP_SCREEN_ON,
                isSaveGameMode = preferences[saveGameModeKey] ?: DEFAULT_SAVE_GAME_MODE
            )
        }

    private companion object {
        private const val PREFERENCES_NAME = "settings"

        private const val KEY_MISTAKES_LIMIT = "mistakes_limit"
        private const val KEY_SHOW_TIMER = "show_timer"
        private const val KEY_RESET_TIMER = "reset_timer"
        private const val KEY_HIGHLIGHT_ERROR_CELLS = "highlight_error_cells"
        private const val KEY_HIGHLIGHT_SIMILAR_CELLS = "highlight_similar_cells"
        private const val KEY_SHOW_REMAINING_NUMBERS = "show_remaining_numbers"
        private const val KEY_HIGHLIGHT_SELECTED_CELL = "highlight_selected_cell"
        private const val KEY_KEEP_SCREEN_ON = "keep_screen_on"
        private const val KEY_SAVE_GAME_MODE = "save_game_mode"
        private const val KEY_GAME_MODE_DIFFICULTY = "game_mode_difficulty"
        private const val KEY_GAME_MODE_TYPE = "game_mode_type"

        private const val DEFAULT_MISTAKES_LIMIT = true
        private const val DEFAULT_SHOW_TIMER = true
        private const val DEFAULT_RESET_TIMER = false
        private const val DEFAULT_HIGHLIGHT_ERROR_CELLS = true
        private const val DEFAULT_HIGHLIGHT_SIMILAR_CELLS = true
        private const val DEFAULT_SHOW_REMAINING_NUMBERS = true
        private const val DEFAULT_HIGHLIGHT_SELECTED_CELL = true
        private const val DEFAULT_KEEP_SCREEN_ON = false
        private const val DEFAULT_SAVE_GAME_MODE = true
        private const val DEFAULT_GAME_MODE_DIFFICULTY = 1
        private const val DEFAULT_GAME_MODE_TYPE = 2
    }
}
