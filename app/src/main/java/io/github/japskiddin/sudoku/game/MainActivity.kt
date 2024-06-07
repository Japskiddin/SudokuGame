package io.github.japskiddin.sudoku.game

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.github.japskiddin.sudoku.game.ui.MainScreen

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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#801b1b1b"))
        )
        setContent {
            MainScreen()
        }
    }
}
