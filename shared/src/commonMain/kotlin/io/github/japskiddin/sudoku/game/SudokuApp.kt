package io.github.japskiddin.sudoku.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.utils.LocalAppVersion
import io.github.japskiddin.sudoku.game.di.AppGraph
import io.github.japskiddin.sudoku.game.ui.MainScreen

@Composable
public fun SudokuApp(
    graph: AppGraph,
    version: String,
    onRateApp: (() -> Boolean)? = null,
    onShareApp: (() -> Unit)? = null,
) {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory,
        LocalAppVersion provides version,
    ) {
        SudokuTheme { MainScreen(onRateApp = onRateApp, onShareApp = onShareApp) }
    }
}
