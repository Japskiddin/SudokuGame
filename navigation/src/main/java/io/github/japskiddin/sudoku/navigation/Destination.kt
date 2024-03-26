package io.github.japskiddin.sudoku.navigation

sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    data object HomeScreen : NoArgumentsDestination(HOME_SCREEN)

    data object GameScreen : NoArgumentsDestination(GAME_SCREEN)

    companion object {
        private const val HOME_SCREEN = "home"
        private const val GAME_SCREEN = "game"
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