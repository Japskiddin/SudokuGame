import android.util.Log
import io.github.japskiddin.sudoku.core.common.Logger

@Suppress("FunctionName")
public fun AndroidLogcatLogger(): Logger = object : Logger {
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
