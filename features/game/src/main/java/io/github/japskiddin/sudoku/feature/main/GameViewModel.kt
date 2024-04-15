package io.github.japskiddin.sudoku.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.usecase.GenerateGameLevelUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val generateGameLevelUseCase: Provider<GenerateGameLevelUseCase>,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.None)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    init {
        onGenerateGameLevel()
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

    private fun onGenerateGameLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading }
            val gameLevel = generateGameLevelUseCase.get().invoke()
            _state.update { State.Success(gameLevel) }
        }
    }
}

internal sealed class State {
    data object None : State()
    data object Loading : State()
    class Error(val errorMessage: String) : State()
    class Success(val gameLevel: GameLevel) : State()
}