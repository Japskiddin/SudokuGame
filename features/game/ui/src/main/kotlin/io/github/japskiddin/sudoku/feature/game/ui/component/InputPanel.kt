package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnPrimary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun InputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    board: ImmutableList<ImmutableList<BoardCell>>,
    @Suppress("MagicNumber")
    gameType: GameType = when (board.size) {
        6 -> GameType.DEFAULT6X6
        9 -> GameType.DEFAULT9X9
        12 -> GameType.DEFAULT12X12
        else -> GameType.UNSPECIFIED
    }
) {
    if (gameType == GameType.DEFAULT12X12) {
        TwoColumnInputPanel(
            modifier = modifier,
            onClick = { value -> onClick(value) },
            board = board
        )
    } else {
        InputPanelContent(
            modifier = modifier,
            onClick = { value -> onClick(value) },
            values = IntRange(start = 1, endInclusive = gameType.size),
            board = board
        )
    }
}

@Composable
private fun TwoColumnInputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    board: ImmutableList<ImmutableList<BoardCell>>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputPanelContent(
            modifier = modifier,
            onClick = { value -> onClick(value) },
            values = IntRange(start = 1, endInclusive = 9),
            board = board
        )
        InputPanelContent(
            modifier = modifier,
            onClick = { value -> onClick(value) },
            values = IntRange(start = 10, endInclusive = 12),
            board = board
        )
    }
}

@Composable
private fun InputPanelContent(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    values: IntRange,
    board: ImmutableList<ImmutableList<BoardCell>>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in values) {
            val counter = countBoardByValue(board, i)
            InputButton(
                value = i.toString(),
                counter = counter,
                modifier = Modifier.clickable {
                    if (counter > 0) {
                        onClick(i)
                    }
                }
            )
        }
    }
}

@Composable
private fun InputButton(
    modifier: Modifier = Modifier,
    value: String,
    counter: Int,
    valueTextSize: TextUnit = 16.sp,
    counterTextSize: TextUnit = 10.sp,
    textColor: Color = OnPrimary
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (counter > 0) {
            textColor
        } else {
            textColor.copy(alpha = 0.2f)
        }
        Text(
            text = value,
            fontSize = valueTextSize,
            color = color
        )
        Text(
            text = counter.toString(),
            fontSize = counterTextSize,
            color = color
        )
    }
}

private fun countBoardByValue(
    list: ImmutableList<ImmutableList<BoardCell>>,
    value: Int
): Int {
    var count = 0
    list.forEach { cells ->
        cells.forEach { cell ->
            if (cell.value == value) count++
        }
    }
    return list.count() - count
}

@Preview(
    name = "Input Panel",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun InputPanelPreview(
    @PreviewParameter(InputPanelPreviewProvider::class) board: ImmutableList<ImmutableList<BoardCell>>
) {
    InputPanel(
        board = board,
        onClick = {}
    )
}

@Preview(
    name = "Input Button",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun InputButtonPreview() {
    SudokuTheme {
        InputButton(
            value = "2",
            counter = 3
        )
    }
}

private class InputPanelPreviewProvider : PreviewParameterProvider<ImmutableList<ImmutableList<BoardCell>>> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "760000009040500800090006364500040041904070000836900000000080900000006007407000580",
        solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty = GameDifficulty.INTERMEDIATE,
        type = GameType.DEFAULT9X9
    )
    private val parsedBoard = parser.parseBoard(
        board = board.initialBoard,
        gameType = board.type
    ).map { item -> item.toImmutableList() }
        .toImmutableList()

    override val values: Sequence<ImmutableList<ImmutableList<BoardCell>>>
        get() = sequenceOf(parsedBoard)
}
