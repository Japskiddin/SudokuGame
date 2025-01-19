package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.feature.game.ui.utils.findGameTypeBySize
import io.github.japskiddin.sudoku.feature.game.ui.utils.getSampleBoardForPreview

@Composable
internal fun InputPanel(
    board: BoardList,
    showRemainingNumbers: Boolean,
    modifier: Modifier = Modifier,
    gameType: GameType = findGameTypeBySize(board.size),
    onClick: (Int) -> Unit,
) {
    if (gameType == GameType.DEFAULT12X12) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputPanelContentRow(
                modifier = modifier,
                values = IntRange(start = 1, endInclusive = 6),
                board = board,
                showRemainingNumbers = showRemainingNumbers,
            ) { value -> onClick(value) }
            InputPanelContentRow(
                modifier = modifier,
                values = IntRange(start = 7, endInclusive = 12),
                board = board,
                showRemainingNumbers = showRemainingNumbers,
            ) { value -> onClick(value) }
        }
    } else {
        InputPanelContentRow(
            modifier = modifier,
            values = IntRange(start = 1, endInclusive = gameType.size),
            board = board,
            showRemainingNumbers = showRemainingNumbers,
        ) { value -> onClick(value) }
    }
}

@Composable
private fun InputPanelContentRow(
    board: BoardList,
    showRemainingNumbers: Boolean,
    values: IntRange,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        values.forEach { value ->
            val counter = board.countByValue(value)
            InputButton(
                value = value,
                counter = counter,
                showRemainingNumbers = showRemainingNumbers,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun InputButton(
    value: Int,
    counter: Int,
    showRemainingNumbers: Boolean,
    modifier: Modifier = Modifier,
    valueTextSize: TextUnit = 16.sp,
    counterTextSize: TextUnit = 10.sp,
    textColor: Color = SudokuTheme.colors.gamePanelNormal,
    pressedTextColor: Color = SudokuTheme.colors.gamePanelPressed,
    onClick: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonColor = if (isPressed) {
        pressedTextColor
    } else {
        textColor
    }

    val buttonModifier = if (counter > 0) {
        modifier
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = { onClick(value) },
            )
            .padding(6.dp)
    } else {
        modifier.padding(6.dp)
    }

    val color = if (counter > 0) {
        buttonColor
    } else {
        buttonColor.copy(alpha = 0.4f)
    }

    val textModifier = if (showRemainingNumbers) {
        Modifier
    } else {
        Modifier.padding(
            top = 6.dp,
            bottom = 6.dp
        )
    }

    Column(
        modifier = buttonModifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = value.toString(),
            style = SudokuTheme.typography.gameButton.copy(
                fontSize = valueTextSize,
                color = color
            ),
            modifier = textModifier,
        )

        if (showRemainingNumbers) {
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = counter.toString(),
                style = SudokuTheme.typography.gameButton.copy(
                    fontSize = counterTextSize,
                    fontWeight = FontWeight.Normal,
                    color = color
                ),
            )
        }
    }
}

private fun BoardList.countByValue(
    value: Int,
): Int {
    var foundValues = 0
    forEach { cells ->
        foundValues += cells.count { cell -> cell.value == value }
    }
    return count() - foundValues
}

@Preview(
    name = "Input Panel - Portrait",
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Composable
private fun InputPanelPreview() {
    SudokuTheme {
        InputPanel(
            board = getSampleBoardForPreview(),
            gameType = GameType.DEFAULT6X6,
            showRemainingNumbers = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Input Button",
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Composable
private fun InputButtonPreview() {
    SudokuTheme {
        InputButton(
            value = 2,
            counter = 3,
            showRemainingNumbers = true,
            onClick = {},
        )
    }
}
