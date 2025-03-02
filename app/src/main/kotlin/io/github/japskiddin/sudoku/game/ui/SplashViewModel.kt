package io.github.japskiddin.sudoku.game.ui

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel
@Inject
constructor() : ViewModel() {
    private val _isShowSplashScreen = MutableStateFlow(true)

    val isShowSplashScreen: StateFlow<Boolean>
        get() = _isShowSplashScreen.asStateFlow()

    init {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                delay(SPLASH_SCREEN_DURATION)
            }
            _isShowSplashScreen.update { false }
        }
    }

    private companion object {
        private const val SPLASH_SCREEN_DURATION = 3000L
    }
}
