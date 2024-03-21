package io.github.japskiddin.sudoku.feature.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GameScreen() {
    GameScreen(viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(viewModel: GameViewModel) {

}