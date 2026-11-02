package io.github.japskiddin.sudoku.core.game.qqwing

import io.github.japskiddin.sudoku.core.model.GameType
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class QQWingControllerTest {
    @Test
    fun solvesKnownPuzzleWithOneSolution() {
        val puzzle = (
            "530070000600195000098000060800060003400803001700020006" +
                "060000280000419005000080079"
            ).map { it.digitToInt() }.toIntArray()
        val expected = (
            "534678912672195348198342567859761423426853791713924856" +
                "961537284287419635345286179"
            ).map { it.digitToInt() }.toIntArray()
        val controller = QQWingController()

        val solution = controller.solve(puzzle, GameType.DEFAULT9X9)

        assertFalse(controller.isImpossible)
        assertEquals(1, controller.solutionCount)
        assertContentEquals(expected, solution)
    }
}
