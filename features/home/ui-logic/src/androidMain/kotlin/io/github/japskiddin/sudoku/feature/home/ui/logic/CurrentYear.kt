package io.github.japskiddin.sudoku.feature.home.ui.logic

import java.util.Calendar

internal actual fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
