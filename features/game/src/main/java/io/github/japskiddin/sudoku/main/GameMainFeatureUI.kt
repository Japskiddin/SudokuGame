package io.github.japskiddin.sudoku.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen() {
    GameScreen(viewModel = viewModel())
}

@Composable
internal fun GameScreen(viewModel: GameViewModel) {

}