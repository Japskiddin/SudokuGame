package io.github.japskiddin.sudoku.game.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel
@Inject
constructor(
    appNavigator: AppNavigator
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel
}
