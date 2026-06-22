package io.github.japskiddin.sudoku.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.game.ui.GameScreen
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameViewModel
import io.github.japskiddin.sudoku.feature.history.ui.HistoryScreen
import io.github.japskiddin.sudoku.feature.home.ui.HomeScreen
import io.github.japskiddin.sudoku.feature.home.ui.logic.HomeViewModel
import io.github.japskiddin.sudoku.feature.settings.ui.SettingsScreen
import io.github.japskiddin.sudoku.game.App
import io.github.japskiddin.sudoku.game.ui.navigation.NavHost
import io.github.japskiddin.sudoku.game.ui.navigation.NavigationEffect
import io.github.japskiddin.sudoku.game.ui.navigation.composable
import io.github.japskiddin.sudoku.navigation.Destination

@Composable
fun MainScreen() {
    val app = LocalContext.current.applicationContext as App
    val appGraph = app.appGraph
    MainScreen(viewModel = viewModel(factory = MainViewModel.factory(appGraph.navigator)))
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val app = LocalContext.current.applicationContext as App
    val appGraph = app.appGraph
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
                    val vm = viewModel(
                        factory = HomeViewModel.factory(
                            appNavigator = appGraph.navigator,
                            appDispatchers = appGraph.appDispatchers,
                            settingsRepository = appGraph.settingsRepository,
                            boardRepository = appGraph.boardRepository,
                            savedGameRepository = appGraph.savedGameRepository,
                            generateSudokuUseCase = appGraph.generateSudokuUseCase,
                            deleteSavedGameUseCase = appGraph.deleteSavedGameUseCase,
                            getGameModeUseCase = appGraph.getGameModePreferenceUseCase,
                        )
                    )
                    HomeScreen(viewModel = vm)
                }
                composable(destination = Destination.Game(boardUid = null)) {
                    val vm = viewModel(
                        factory = GameViewModel.factory(
                            appNavigator = appGraph.navigator,
                            settingsRepository = appGraph.settingsRepository,
                            savedGameRepository = appGraph.savedGameRepository,
                            getBoardUseCase = appGraph.getBoardUseCase,
                            saveGameUseCase = appGraph.saveGameUseCase,
                            restoreGameUseCase = appGraph.restoreGameUseCase,
                            solveBoardUseCase = appGraph.solveBoardUseCase,
                            checkGameStatusUseCase = appGraph.checkGameStatusUseCase,
                            addToHistoryUseCase = appGraph.addToHistoryUseCase,
                        )
                    )
                    GameScreen(viewModel = vm)
                }
                composable(destination = Destination.Settings()) {
                    val vm = viewModel(
                        factory = SettingsViewModel.factory(
                            appNavigator = appGraph.navigator,
                            settingsRepository = appGraph.settingsRepository,
                        )
                    )
                    SettingsScreen(viewModel = vm)
                }
                composable(destination = Destination.History()) {
                    val vm = viewModel(
                        factory = HistoryViewModel.factory(
                            appNavigator = appGraph.navigator,
                            getHistoryUseCase = appGraph.getHistoryUseCase,
                        )
                    )
                    HistoryScreen(viewModel = vm)
                }
            }
        }
    }
}
