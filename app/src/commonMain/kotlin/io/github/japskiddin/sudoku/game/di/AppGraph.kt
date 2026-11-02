package io.github.japskiddin.sudoku.game.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore

@DependencyGraph(AppScope::class)
public interface AppGraph : ViewModelGraph {
    @DependencyGraph.Factory
    public fun interface Factory {
        public fun create(
            @Provides database: SudokuDatabase,
            @Provides settingsDatastore: SettingsDatastore,
        ): AppGraph
    }
}
