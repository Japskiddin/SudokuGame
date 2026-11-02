package io.github.japskiddin.sudoku.game

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.lifecycle.viewmodel.CreationExtras
import dev.zacsweers.metro.createGraphFactory
import io.github.japskiddin.sudoku.core.ui.resources.Res
import io.github.japskiddin.sudoku.core.ui.resources.app_name
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.game.di.AppGraph
import io.github.japskiddin.sudoku.game.ui.MainViewModel
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppGraphJvmTest {
    @Test
    fun createsViewModelAndLoadsSharedResources() = runTest {
        val directory = Files.createTempDirectory("sudoku-app-test").toFile()
        val database = SudokuDatabase(directory.resolve("sudoku").absolutePath)
        try {
            val settings = SettingsDatastore(
                PreferenceDataStoreFactory.create(
                    scope = backgroundScope,
                    produceFile = { directory.resolve("settings.preferences_pb") },
                )
            )
            val graph = createGraphFactory<AppGraph.Factory>().create(database, settings)

            assertNotNull(graph.metroViewModelFactory.create(MainViewModel::class, CreationExtras.Empty))
            assertTrue(getString(Res.string.app_name).isNotBlank())
        } finally {
            database.close()
            directory.deleteRecursively()
        }
    }
}
