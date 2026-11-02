package io.github.japskiddin.sudoku.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.japskiddin.sudoku.game.di.appGraph
import io.github.japskiddin.sudoku.game.ui.SplashViewModel

class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.isShowSplashScreen.value
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SudokuApp(graph = appGraph(), version = BuildConfig.VERSION_NAME)
        }
    }
}
