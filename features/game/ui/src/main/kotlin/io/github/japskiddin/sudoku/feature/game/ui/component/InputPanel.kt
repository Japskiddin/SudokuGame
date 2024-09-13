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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.sqrt

@Composable
internal fun InputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    board: String,
    gameType: GameType
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
    board: String,
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
    board: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in values) {
            val count = board.countBoardByValue(i.toString())
            InputButton(
                onClick = { onClick(i) },
                value = i.toString(),
                counter = count
            )
        }
    }
}

@Composable
private fun InputButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    value: String,
    counter: Int,
    valueTextSize: TextUnit = 16.sp,
    counterTextSize: TextUnit = 10.sp
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = valueTextSize
        )
        Text(
            text = counter.toString(),
            fontSize = counterTextSize
        )
    }
}

private fun String.countBoardByValue(value: String) =
    sqrt(count().toFloat()).toInt() - toCharArray().count { it.toString() == value }

@Preview(
    name = "Input Panel",
    showBackground = true
)
@Composable
private fun InputPanelPreview() {
    InputPanel(
        board = "760000009040500800090006364500040041904070000836900000000080900000006007407000580",
        gameType = GameType.DEFAULT9X9,
        onClick = {}
    )
}

@Preview(
    name = "Input Button",
    showBackground = true
)
@Composable
private fun InputButtonPreview() {
    SudokuTheme {
        InputButton(
            onClick = {},
            value = "2",
            counter = 3
        )
    }
}

private class InputPanelPreviewProvider : PreviewParameterProvider<ImmutableList<ImmutableList<BoardCell>>> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
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
