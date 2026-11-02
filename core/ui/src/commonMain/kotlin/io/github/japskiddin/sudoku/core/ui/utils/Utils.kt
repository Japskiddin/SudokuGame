package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
public fun isLandscape(): Boolean {
    val size = LocalWindowInfo.current.containerSize
    return size.width > size.height
}
