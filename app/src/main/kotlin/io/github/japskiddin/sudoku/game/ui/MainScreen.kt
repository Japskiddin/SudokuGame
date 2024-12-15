package io.github.japskiddin.sudoku.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.game.ui.GameScreen
import io.github.japskiddin.sudoku.feature.home.ui.HomeScreen
import io.github.japskiddin.sudoku.feature.records.ui.RecordsScreen
import io.github.japskiddin.sudoku.feature.settings.ui.SettingsScreen
import io.github.japskiddin.sudoku.game.ui.navigation.NavHost
import io.github.japskiddin.sudoku.game.ui.navigation.NavigationEffect
import io.github.japskiddin.sudoku.game.ui.navigation.composable
import io.github.japskiddin.sudoku.navigation.Destination

@Composable
fun MainScreen() {
    MainScreen(viewModel = hiltViewModel())
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavigationEffect(
        navigationChannel = viewModel.navigationChannel,
        navHostController = navController
    )

    SudokuTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Destination.Home
            ) {
                composable(destination = Destination.Home) {
                    HomeScreen()
                }
                composable(destination = Destination.Game) {
                    GameScreen()
                }
                composable(destination = Destination.Settings) {
                    SettingsScreen()
                }
                composable(destination = Destination.Records) {
                    RecordsScreen()
                }
            }
        }
    }
}
