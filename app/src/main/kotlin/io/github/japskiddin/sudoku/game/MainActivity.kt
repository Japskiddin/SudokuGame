package io.github.japskiddin.sudoku.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.game.di.appGraph
import io.github.japskiddin.sudoku.game.ui.MainScreen
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
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides appGraph().metroViewModelFactory,
            ) {
                SudokuTheme {
                    MainScreen()
                }
            }
        }
    }
}
