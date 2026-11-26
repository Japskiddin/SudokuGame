package io.github.japskiddin.sudoku.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.resources.Res
import io.github.japskiddin.sudoku.core.ui.resources.please_wait
import io.github.japskiddin.sudoku.feature.about.ui.AboutScreen
import io.github.japskiddin.sudoku.feature.game.ui.GameScreen
import io.github.japskiddin.sudoku.feature.history.ui.HistoryScreen
import io.github.japskiddin.sudoku.feature.home.ui.HomeScreen
import io.github.japskiddin.sudoku.feature.onboarding.ui.OnboardingScreen
import io.github.japskiddin.sudoku.feature.settings.ui.SettingsScreen
import io.github.japskiddin.sudoku.game.ui.MainViewModel.OnboardingStatus
import io.github.japskiddin.sudoku.game.ui.navigation.NavHost
import io.github.japskiddin.sudoku.game.ui.navigation.NavigationEffect
import io.github.japskiddin.sudoku.game.ui.navigation.composable
import io.github.japskiddin.sudoku.navigation.Destination
import io.github.japskiddin.sudoku.navigation.NavigationIntent
import kotlinx.coroutines.channels.Channel

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
    val onboardingStatus by viewModel.onboardingStatus.collectAsStateWithLifecycle()
    when (onboardingStatus) {
        OnboardingStatus.Loading -> Loading(
            resId = Res.string.please_wait,
            modifier = Modifier.fillMaxSize(),
        )

        OnboardingStatus.Completed -> MainNavigation(viewModel.navigationChannel, onRateApp, onShareApp)

        else -> OnboardingScreen(
            busy = onboardingStatus == OnboardingStatus.Saving,
            saveFailed = onboardingStatus == OnboardingStatus.SaveFailed,
            onFinish = viewModel::completeOnboarding,
        )
    }
}

@Composable
private fun MainNavigation(
    navigationChannel: Channel<NavigationIntent>,
    onRateApp: (() -> Boolean)?,
    onShareApp: (() -> Unit)?,
) {
    val navController = rememberNavController()

    NavigationEffect(
        navigationChannel = navigationChannel,
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
