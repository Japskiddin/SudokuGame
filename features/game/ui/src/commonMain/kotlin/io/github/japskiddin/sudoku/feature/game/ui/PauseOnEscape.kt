package io.github.japskiddin.sudoku.feature.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun Modifier.pauseOnEscape(onPause: () -> Unit): Modifier
