package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.feature.game.ui.R
import io.github.japskiddin.sudoku.feature.game.ui.utils.ToolAction
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ToolPanel(
    modifier: Modifier = Modifier,
    onToolClick: (ToolAction) -> Unit
) {
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    if (showResetDialog) {
        ResetDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                showResetDialog = false
                onToolClick(ToolAction.RESET)
            }
        )
    }

    if (isLandscape()) {
        LandscapeToolPanel(
            modifier = modifier,
            onShowDialog = { showResetDialog = true },
            onToolClick = onToolClick
        )
    } else {
        PortraitToolPanel(
            modifier = modifier,
            onShowDialog = { showResetDialog = true },
            onToolClick = onToolClick
        )
    }
}

@Composable
private fun LandscapeToolPanel(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    onToolClick: (ToolAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_eraser),
            icon = painterResource(id = R.drawable.ic_tool_eraser),
            showDescription = false,
        ) { onToolClick(ToolAction.ERASER) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_undo),
            icon = painterResource(id = R.drawable.ic_tool_undo),
            showDescription = false,
        ) { onToolClick(ToolAction.UNDO) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_redo),
            icon = painterResource(id = R.drawable.ic_tool_redo),
            showDescription = false,
        ) { onToolClick(ToolAction.REDO) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_note),
            icon = painterResource(id = R.drawable.ic_tool_note),
            showDescription = false,
        ) { onToolClick(ToolAction.NOTE) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_reset),
            icon = painterResource(id = R.drawable.ic_tool_reset),
            showDescription = false,
        ) { onShowDialog() }
    }
}

@Composable
private fun PortraitToolPanel(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    onToolClick: (ToolAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_eraser),
            icon = painterResource(id = R.drawable.ic_tool_eraser)
        ) { onToolClick(ToolAction.ERASER) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_undo),
            icon = painterResource(id = R.drawable.ic_tool_undo)
        ) { onToolClick(ToolAction.UNDO) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_redo),
            icon = painterResource(id = R.drawable.ic_tool_redo)
        ) { onToolClick(ToolAction.REDO) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_note),
            icon = painterResource(id = R.drawable.ic_tool_note)
        ) { onToolClick(ToolAction.NOTE) }
        ToolButton(
            text = stringResource(id = CoreUiR.string.tool_reset),
            icon = painterResource(id = R.drawable.ic_tool_reset)
        ) { onShowDialog() }
    }
}

@Composable
private fun ToolButton(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    textSize: TextUnit = 16.sp,
    textColor: Color = SudokuTheme.colors.gamePanelNormal,
    pressedTextColor: Color = SudokuTheme.colors.gamePanelPressed,
    showDescription: Boolean = true,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonColor = if (isPressed) {
        pressedTextColor
    } else {
        textColor
    }

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick,
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            colorFilter = ColorFilter.tint(color = buttonColor),
            modifier = Modifier.size(iconSize)
        )
        if (showDescription) {
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = text,
                style = SudokuTheme.typography.toolButton.copy(
                    color = buttonColor,
                    fontSize = textSize
                ),
            )
        }
    }
}

@Preview(
    name = "Tool Panel - Portrait",
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Preview(
    name = "Tool Panel - Landscape",
    widthDp = 732,
    heightDp = 412,
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Composable
private fun ToolPanelPreview() {
    SudokuTheme {
        ToolPanel(
            onToolClick = {}
        )
    }
}
