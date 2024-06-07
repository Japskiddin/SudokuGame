package io.github.japskiddin.sudoku.core.game.qqwing

enum class GameType(
    val size: Int,
    val sectionHeight: Int,
    val sectionWidth: Int,
) {
    UNSPECIFIED(size = 1, sectionHeight = 1, sectionWidth = 1),
    DEFAULT6X6(size = 6, sectionHeight = 2, sectionWidth = 3),
    DEFAULT9X9(size = 9, sectionHeight = 3, sectionWidth = 3),
    DEFAULT12X12(size = 12, sectionHeight = 3, sectionWidth = 4),
}