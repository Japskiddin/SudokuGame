package io.github.japskiddin.sudoku.feature.records.ui.logic

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
public class RecordsViewModel
@Inject
internal constructor(
    private val appNavigator: AppNavigator,
) : ViewModel()
