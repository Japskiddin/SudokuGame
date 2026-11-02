package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

private const val PanelWidthPercentLandscape: Float = .6f
private const val PanelWidthPercentPortrait: Float = 1f

@Composable
public fun Modifier.panelBackground(): Modifier = this
    .padding(16.dp)
    .fillMaxWidth(
        if (isLandscape()) {
            PanelWidthPercentLandscape
        } else {
            PanelWidthPercentPortrait
        }
    )
    .dialogBackground()

@Composable
public fun Modifier.dialogBackground(): Modifier = this
    .shadow(8.dp, shape = RoundedCornerShape(16.dp))
    .background(
        color = SudokuTheme.colors.dialog,
        shape = RoundedCornerShape(size = 16.dp)
    )
    .padding(16.dp)

@Composable
public expect fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 1.dp,
    spread: Dp = 0.dp,
): Modifier
