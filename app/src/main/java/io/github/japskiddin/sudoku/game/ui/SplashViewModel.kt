package io.github.japskiddin.sudoku.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor() : ViewModel() {
    private val _isShowSplashScreen = MutableStateFlow(true)
    val isShowSplashScreen = _isShowSplashScreen.asStateFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DURATION)
            _isShowSplashScreen.value = false
        }
    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 3000L
    }
}
