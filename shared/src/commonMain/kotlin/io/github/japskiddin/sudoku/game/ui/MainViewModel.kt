package io.github.japskiddin.sudoku.game.ui

import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class MainViewModel(
    appNavigator: AppNavigator,
    private val settingsDatastore: SettingsDatastore,
    private val dispatchers: AppDispatchers,
) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel

    private val mutableOnboardingStatus = MutableStateFlow(OnboardingStatus.Loading)
    val onboardingStatus = mutableOnboardingStatus.asStateFlow()

    init {
        viewModelScope.launch(dispatchers.io) {
            val isCompleted = try {
                settingsDatastore.isOnboardingCompleted.first()
            } catch (_: IOException) {
                false
            }
            mutableOnboardingStatus.value = if (isCompleted) OnboardingStatus.Completed else OnboardingStatus.Showing
        }
    }

    fun completeOnboarding() {
        val status = mutableOnboardingStatus.value
        if (status != OnboardingStatus.Showing && status != OnboardingStatus.SaveFailed) return
        if (!mutableOnboardingStatus.compareAndSet(status, OnboardingStatus.Saving)) return

        viewModelScope.launch(dispatchers.io) {
            mutableOnboardingStatus.value = try {
                settingsDatastore.completeOnboarding()
                OnboardingStatus.Completed
            } catch (_: IOException) {
                OnboardingStatus.SaveFailed
            }
        }
    }

    enum class OnboardingStatus {
        Loading,
        Showing,
        Saving,
        SaveFailed,
        Completed,
    }
}
