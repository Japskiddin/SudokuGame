package io.github.japskiddin.sudoku.navigation

import kotlinx.coroutines.channels.Channel

public interface AppNavigator {
    public val navigationChannel: Channel<NavigationIntent>

    public suspend fun navigateBack(
        route: String? = null,
        inclusive: Boolean = false
    )

    public fun tryNavigateBack(
        route: String? = null,
        inclusive: Boolean = false
    )

    public suspend fun navigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false
    )

    public fun tryNavigateTo(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false
    )
}
