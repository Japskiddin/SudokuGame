package io.github.japskiddin.sudoku.core.common.android

import android.util.Log
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.japskiddin.sudoku.core.common.Logger

@ContributesBinding(AppScope::class)
@Inject
public class AndroidLogcatLogger : Logger {
    override fun d(
        tag: String,
        message: String
    ) {
        Log.d(tag, message)
    }

    override fun e(
        tag: String,
        message: String
    ) {
        Log.e(tag, message)
    }
}
