package io.github.japskiddin.sudoku.desktop

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.zacsweers.metro.createGraphFactory
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import io.github.japskiddin.sudoku.game.SudokuApp
import io.github.japskiddin.sudoku.game.di.AppGraph
import java.io.File
import javax.imageio.ImageIO

private object DesktopResources {
    val version: String = open("/sudoku-version.txt")
        .bufferedReader().use { it.readText().trim() }

    val icon = open("/sudoku-icon.png").use {
        BitmapPainter(ImageIO.read(it).toComposeImageBitmap())
    }

    private fun open(path: String) = checkNotNull(javaClass.getResourceAsStream(path)) {
        "Missing desktop resource $path"
    }
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
        Window(onCloseRequest = ::exitApplication, title = "Sudoku", icon = DesktopResources.icon) {
            SudokuApp(graph = graph, version = DesktopResources.version)
        }
    }
}
