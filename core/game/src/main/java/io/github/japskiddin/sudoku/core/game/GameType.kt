package io.github.japskiddin.sudoku.core.game

public enum class GameType(
    public val size: Int,
    public val sectionHeight: Int,
    public val sectionWidth: Int,
    public val title: String
) {
    UNSPECIFIED(size = 1, sectionHeight = 1, sectionWidth = 1, title = "1x1"),
    DEFAULT6X6(size = 6, sectionHeight = 2, sectionWidth = 3, title = "6x6"),
    DEFAULT9X9(size = 9, sectionHeight = 3, sectionWidth = 3, title = "9x9"),
    DEFAULT12X12(size = 12, sectionHeight = 3, sectionWidth = 4, title = "12x12")
}
