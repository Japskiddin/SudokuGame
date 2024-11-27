package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
public fun SudokuTheme(
    typography: Typography = SudokuTheme.typography,
    content: @Composable () -> Unit
) {
    val colors = remember {
        ThemeColors.copy()
    }.apply {
        updateColorsFrom(ThemeColors)
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography
    ) {
        content()
    }
}

public object SudokuTheme {
    public val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    public val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
