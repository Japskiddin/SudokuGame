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
  internal val currentYear: String
    get() = Calendar.getInstance().get(Calendar.YEAR).toString()

  internal fun onStartClick() {
  }

  internal fun onSettingsClick() {
    TODO("In Development")
  }

  internal fun onRecordsClick() {
    TODO("In Development")
  }

  private fun navigateToGame() {
    appNavigator.tryNavigateTo(Destination.GameScreen())
  }
}