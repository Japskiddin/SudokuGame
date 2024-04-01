package io.github.japskiddin.sudoku.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.usecase.GenerateGameLevelUseCase
import io.github.japskiddin.sudoku.feature.main.utils.toState
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class GameViewModel @Inject constructor(
    generateGameLevelUseCase: Provider<GenerateGameLevelUseCase>,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    val state: StateFlow<State> = generateGameLevelUseCase.get().invoke()
//        .onStart { emit(State.Loading()) }
        .map { it.toState() }
//        .catch { emit(State.Error()) }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

//    val state: StateFlow<State> = getGameLevelUseCase.get().invoke()
//        .map { it.toState() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

internal sealed class State {
    object None : State()
    class Loading : State()
    class Error(val errorMessage: String) : State()
    class Success(val gameLevel: GameLevel) : State()
}