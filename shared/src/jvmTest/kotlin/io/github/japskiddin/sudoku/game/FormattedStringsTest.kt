package io.github.japskiddin.sudoku.game

import io.github.japskiddin.sudoku.core.ui.resources.Res
import io.github.japskiddin.sudoku.core.ui.resources.current_mistakes
import io.github.japskiddin.sudoku.core.ui.resources.play_time
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.test.assertContains

class FormattedStringsTest {
    @Test
    fun substitutesNumberAndTextArguments() = runTest {
        val mistakes = getString(Res.string.current_mistakes, 2, 5)

        assertContains(mistakes, "2")
        assertContains(mistakes, "5")
        assertContains(getString(Res.string.play_time, "12:34"), "12:34")
    }
}
