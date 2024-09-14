package io.github.japskiddin.sudoku.feature.game.ui.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class BoardPreviewProvider : PreviewParameterProvider<ImmutableList<ImmutableList<BoardCell>>> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "760000009040500800090006364500040041904070000836900000000080900000006007407000580",
        solvedBoard = "768432159143569872295817364572348691914675238836921745651784923389256417427193586",
        difficulty = GameDifficulty.INTERMEDIATE,
        type = GameType.DEFAULT9X9
    )
    private val parsedBoard = parser.parseBoard(
        board = board.initialBoard,
        gameType = board.type
    ).map { item -> item.toImmutableList() }.toImmutableList()

    override val values: Sequence<ImmutableList<ImmutableList<BoardCell>>>
        get() = sequenceOf(parsedBoard)
}
