package io.github.japskiddin.sudoku.game.ui

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.japskiddin.sudoku.navigation.AppNavigator

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class MainViewModel(
    appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel
}
