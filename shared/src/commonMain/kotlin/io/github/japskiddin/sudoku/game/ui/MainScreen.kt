package io.github.japskiddin.sudoku.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.japskiddin.sudoku.feature.about.ui.AboutScreen
import io.github.japskiddin.sudoku.feature.game.ui.GameScreen
import io.github.japskiddin.sudoku.feature.history.ui.HistoryScreen
import io.github.japskiddin.sudoku.feature.home.ui.HomeScreen
import io.github.japskiddin.sudoku.feature.settings.ui.SettingsScreen
import io.github.japskiddin.sudoku.game.ui.navigation.NavHost
import io.github.japskiddin.sudoku.game.ui.navigation.NavigationEffect
import io.github.japskiddin.sudoku.game.ui.navigation.composable
import io.github.japskiddin.sudoku.navigation.Destination

@Composable
public fun MainScreen(
    onRateApp: (() -> Boolean)? = null,
    onShareApp: (() -> Unit)? = null,
) {
    MainScreen(viewModel = metroViewModel(), onRateApp = onRateApp, onShareApp = onShareApp)
}

@Composable
private fun MainScreen(
    viewModel: MainViewModel,
    onRateApp: (() -> Boolean)?,
    onShareApp: (() -> Unit)?,
) {
    val navController = rememberNavController()

    NavigationEffect(
        navigationChannel = viewModel.navigationChannel,
        navHostController = navController
    )

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Destination.Home
        ) {
            composable(destination = Destination.Home) { HomeScreen() }
            composable(destination = Destination.Game) { GameScreen() }
            composable(destination = Destination.Settings) { SettingsScreen() }
            composable(destination = Destination.About) {
                AboutScreen(onRateApp = onRateApp, onShareApp = onShareApp)
            }
            composable(destination = Destination.History) { HistoryScreen() }
        }
    }
}
