package io.github.japskiddin.sudoku.navigation

public sealed class Destination(
    protected val route: String,
    vararg params: String,
) {
    public val fullRoute: String = if (params.isEmpty()) {
        route
    } else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{$it}") }
        builder.toString()
    }

    public sealed class NoArgumentsDestination(route: String) : Destination(route) {
        public operator fun invoke(): String = route
    }

    public data object Home : NoArgumentsDestination(DESTINATION_HOME)

    public data object Game : Destination(DESTINATION_GAME, KEY_BOARD_UID) {
        public operator fun invoke(boardUid: Long): String = route.appendParams(KEY_BOARD_UID to boardUid)
    }

    public data object Settings : NoArgumentsDestination(DESTINATION_SETTINGS)

    public data object Records : NoArgumentsDestination(DESTINATION_RECORDS)

    public companion object {
        private const val DESTINATION_HOME = "home"
        private const val DESTINATION_GAME = "game"
        private const val DESTINATION_SETTINGS = "settings"
        private const val DESTINATION_RECORDS = "records"

        public const val KEY_BOARD_UID: String = "board_uid"
    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)
    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}
