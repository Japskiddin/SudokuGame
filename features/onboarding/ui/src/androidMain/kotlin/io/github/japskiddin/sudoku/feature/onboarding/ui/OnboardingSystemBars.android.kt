package io.github.japskiddin.sudoku.feature.onboarding.ui

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
internal actual fun OnboardingSystemBars(darkIcons: Boolean) {
    val view = LocalView.current
    val activity = generateSequence(view.context) { (it as? ContextWrapper)?.baseContext }
        .filterIsInstance<Activity>()
        .firstOrNull()
    val window = activity?.window ?: return

    DisposableEffect(window, view, darkIcons) {
        val controller = WindowCompat.getInsetsController(window, view)
        val hadDarkStatusBarIcons = controller.isAppearanceLightStatusBars
        val hadDarkNavigationBarIcons = controller.isAppearanceLightNavigationBars
        controller.isAppearanceLightStatusBars = darkIcons
        controller.isAppearanceLightNavigationBars = darkIcons
        onDispose {
            controller.isAppearanceLightStatusBars = hadDarkStatusBarIcons
            controller.isAppearanceLightNavigationBars = hadDarkNavigationBarIcons
        }
    }
}
