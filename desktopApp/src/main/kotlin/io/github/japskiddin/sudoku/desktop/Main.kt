package io.github.japskiddin.sudoku.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.zacsweers.metro.createGraphFactory
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.game.SudokuApp
import io.github.japskiddin.sudoku.game.di.AppGraph
import java.io.File

private object DesktopVersion {
    val name: String = checkNotNull(javaClass.getResourceAsStream("/sudoku-version.txt"))
        .bufferedReader().use { it.readText().trim() }
}

fun main() {
    val dataDirectory = File(System.getProperty("user.home"), ".sudoku").apply {
        check(isDirectory || mkdirs()) { "Cannot create Sudoku data directory: $this" }
    }
    val graph = createGraphFactory<AppGraph.Factory>().create(
        database = SudokuDatabase(File(dataDirectory, "sudoku").absolutePath),
        settingsDatastore = SettingsDatastore(File(dataDirectory, "settings.preferences_pb")),
    )
    application {
        Window(onCloseRequest = ::exitApplication, title = "Sudoku") {
            SudokuApp(graph = graph, version = DesktopVersion.name)
        }
    }
}
