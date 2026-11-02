package io.github.japskiddin.sudoku.navigation

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationTest {
    @Test
    fun destinationBuildsRoutesWithAndWithoutArguments() {
        assertEquals("home", Destination.Home())
        assertEquals("game/{board_uid}", Destination.Game.fullRoute)
        assertEquals("game/25", Destination.Game(25))
        assertEquals("settings/section", "settings".appendParams("section" to "section"))
    }

    @Test
    fun navigatorSendsNavigateToIntent() = runTest {
        val navigator = AppNavigatorImpl()

        navigator.navigateTo("game/12", popUpToRoute = "home", inclusive = true, isSingleTop = true)

        assertEquals(
            NavigationIntent.NavigateTo(
                route = "game/12",
                popUpToRoute = "home",
                inclusive = true,
                isSingleTop = true,
            ),
            navigator.navigationChannel.receive(),
        )
    }

    @Test
    fun navigatorTryNavigateBackSendsBackIntent() = runTest {
        val navigator = AppNavigatorImpl()

        navigator.tryNavigateBack(route = "home", inclusive = false)

        assertEquals(
            NavigationIntent.NavigateBack(route = "home", inclusive = false),
            navigator.navigationChannel.receive(),
        )
    }
}
