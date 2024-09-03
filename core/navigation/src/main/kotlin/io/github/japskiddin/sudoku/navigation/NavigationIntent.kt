package io.github.japskiddin.sudoku.navigation

public sealed class NavigationIntent {
    public data class NavigateBack(
        val route: String? = null,
        val inclusive: Boolean = false
    ) : NavigationIntent()

    public data class NavigateTo(
        val route: String,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false
    ) : NavigationIntent()
}
