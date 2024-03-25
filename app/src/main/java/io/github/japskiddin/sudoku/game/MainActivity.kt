package io.github.japskiddin.sudoku.game

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.japskiddin.sudoku.feature.home.HomeScreen
import io.github.japskiddin.sudoku.game.ui.theme.SudokuTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var backPressedMillis = 0L

    private val finish: () -> Unit = {
        if (backPressedMillis + 3000 > System.currentTimeMillis()) {
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#801b1b1b"))
        )
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        SudokuTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                color = MaterialTheme.colorScheme.background
            ) {
                HomeScreen()
            }
        }
    }
}