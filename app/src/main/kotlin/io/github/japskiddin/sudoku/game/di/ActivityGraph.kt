package io.github.japskiddin.sudoku.game.di

import android.app.Activity
import android.view.Window
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import io.github.japskiddin.sudoku.core.common.di.ActivityScope

@GraphExtension(ActivityScope::class)
interface ActivityGraph {

    @Provides
    fun providesWindow(activity: Activity): Window = activity.window

    @GraphExtension.Factory
    interface Factory {
        fun createActivityGraph(@Provides activity: Activity): ActivityGraph
    }
}
