package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalAppVersion: ProvidableCompositionLocal<String> = staticCompositionLocalOf { "" }
