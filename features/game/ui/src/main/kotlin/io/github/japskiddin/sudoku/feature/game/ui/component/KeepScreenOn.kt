package io.github.japskiddin.sudoku.feature.game.ui.component

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

private const val FlagKeepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

@Composable
internal fun KeepScreenOn(
    isEnabled: Boolean
) {
    if (!LocalInspectionMode.current) {
        val activity = LocalContext.current as Activity

        DisposableEffect(Unit) {
            val window = activity.window
            if (isEnabled) {
                window.addFlags(FlagKeepScreenOn)
            }

            onDispose {
                window.clearFlags(FlagKeepScreenOn)
            }
        }
    }
}
