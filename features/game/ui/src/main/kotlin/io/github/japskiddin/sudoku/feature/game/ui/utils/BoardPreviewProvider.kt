package io.github.japskiddin.sudoku.feature.game.ui.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.japskiddin.sudoku.core.game.utils.BoardList
import io.github.japskiddin.sudoku.core.game.utils.convertToList
import io.github.japskiddin.sudoku.core.game.utils.toImmutable
import io.github.japskiddin.sudoku.core.model.GameType

internal class BoardPreviewProvider : PreviewParameterProvider<BoardList> {
    private val board = "760000009040500800090006364500040041904070000836900000000080900000006007407000580"
    private val parsedBoard = board.convertToList(GameType.DEFAULT9X9).toImmutable()

    override val values: Sequence<BoardList>
        get() = sequenceOf(parsedBoard)
}
