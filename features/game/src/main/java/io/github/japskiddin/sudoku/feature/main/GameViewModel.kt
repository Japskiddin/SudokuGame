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
//    val state: StateFlow<State> = generateGameLevelUseCase.get().invoke()
//        .map { it.toState() }
//        .catch { emit(State.Error("TEST")) }
//        .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading)

    init {
        onGenerateGameLevel()
    }

    fun onGenerateGameLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading }
            val gameLevel = generateGameLevelUseCase.get().invoke()
            _state.update { State.Success(gameLevel) }
        }
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

//    val state: StateFlow<State> = getGameLevelUseCase.get().invoke()
//        .map { it.toState() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

internal sealed class State {
    data object None : State()
    data object Loading : State()
    class Error(val errorMessage: String) : State()
    class Success(val gameLevel: GameLevel) : State()
}