package io.github.japskiddin.sudoku.game_main

import androidx.lifecycle.ViewModel
import io.github.japskiddin.sudoku.game_data.models.GameLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(State.None)

    val state: StateFlow<State>
        get() = _state.asStateFlow()

    sealed class State {
        object None : State()
        class Loading : State()
        class Error(val errorMessage: String) : State()
        class Success(val gameLevel: GameLevel) : State()
    }
}