package io.github.japskiddin.sudoku.game.di

import android.content.Context
import io.github.japskiddin.sudoku.core.common.di.MetroGraphHolder

internal fun Context.appGraph(): AppGraph =
    (applicationContext as MetroGraphHolder).metroGraph as AppGraph
