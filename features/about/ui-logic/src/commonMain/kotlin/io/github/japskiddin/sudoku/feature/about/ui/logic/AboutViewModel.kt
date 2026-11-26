package io.github.japskiddin.sudoku.feature.about.ui.logic

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.japskiddin.sudoku.navigation.AppNavigator

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
public class AboutViewModel(
    private val appNavigator: AppNavigator,
) : ViewModel() {
    public fun onBack() {
        appNavigator.tryNavigateBack()
    }
}
