package io.github.japskiddin.sudoku.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.common.android.di.ViewModelKey
import io.github.japskiddin.sudoku.navigation.AppNavigator

@Inject
@ViewModelKey
internal class MainViewModel(
    appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel

    companion object {
        fun factory(appNavigator: AppNavigator): ViewModelProvider.Factory = viewModelFactory {
            initializer { MainViewModel(appNavigator = appNavigator) }
        }
    }
}
