package io.github.japskiddin.sudoku.navigation

public sealed class Destination(protected val route: String, vararg params: String) {
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

    public data object HomeScreen : NoArgumentsDestination(HOME_SCREEN)

    public data object GameScreen : Destination(GAME_SCREEN, KEY_BOARD_UID) {
        public operator fun invoke(boardUid: Long): String = route.appendParams(KEY_BOARD_UID to boardUid)
    }

    public companion object {
        private const val HOME_SCREEN = "home"
        private const val GAME_SCREEN = "game"

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
