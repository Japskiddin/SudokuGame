package io.github.japskiddin.sudoku.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
) : ViewModel() {
    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR).toString()
    }

    fun navigateToGame() {
        appNavigator.tryNavigateTo(Destination.GameScreen())
    }
}