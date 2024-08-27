package io.github.japskiddin.sudoku.game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.japskiddin.sudoku.game.ui.MainScreen
import io.github.japskiddin.sudoku.game.ui.SplashViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    private var backPressedMillis = 0L

    @Suppress("unused")
    private val finish: () -> Unit = {
        if (backPressedMillis + BACK_PRESSED_DELAY > System.currentTimeMillis()) {
            finishAndRemoveTask()
        } else {
            Toast.makeText(
                applicationContext,
                R.string.press_back_again_to_exit,
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressedMillis = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isShowSplashScreen.value
        }
        enableEdgeToEdge()
        hideSystemUi()
        setContent {
            MainScreen()
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        private const val BACK_PRESSED_DELAY = 3000L
    }
}
