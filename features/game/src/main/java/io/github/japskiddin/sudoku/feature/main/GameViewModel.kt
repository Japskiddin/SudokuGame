package io.github.japskiddin.sudoku.feature.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.feature.main.usecase.GenerateGameLevelUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class GameViewModel @Inject constructor(
    generateGameLevelUseCase: Provider<GenerateGameLevelUseCase>,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val _state = MutableStateFlow(State.None)

    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }

//    val state: StateFlow<State> = getGameLevelUseCase.get().invoke()
//        .map { it.toState() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    sealed class State {
        object None : State()
        class Loading : State()
        class Error(val errorMessage: String) : State()
        class Success(val gameLevel: GameLevel) : State()
    }
}