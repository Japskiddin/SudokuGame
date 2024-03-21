package io.github.japskiddin.sudoku.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : ViewModel() {
    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR).toString()
    }
}