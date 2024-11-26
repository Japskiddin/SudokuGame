package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedTrackColor: Color = SudokuTheme.colors.switchCheckedTrack,
    uncheckedTrackColor: Color = SudokuTheme.colors.switchUncheckedTrack,
    checkThumbColor: Color = SudokuTheme.colors.switchCheckedThumb,
    uncheckThumbColor: Color = SudokuTheme.colors.switchUncheckedThumb,
) {
    val thumbPosition by animateFloatAsState(
        targetValue = if (checked) {
            1f
        } else {
            0f
        },
        label = "thumb position"
    )
    val circleRadius = remember { 12.dp }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(width = 56.dp, height = 32.dp)
            .background(color = Color.Transparent)
            .clickable(
                onClick = { onCheckedChange(!checked) },
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val trackColor = if (checked) {
                checkedTrackColor
            } else {
                uncheckedTrackColor
            }
            val thumbColor = if (checked) {
                checkThumbColor
            } else {
                uncheckThumbColor
            }

            drawRoundRect(
                color = trackColor,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(x = 18.dp.toPx(), y = 18.dp.toPx())
            )

            val thumbOffset = calculateThumbOffset(
                start = 16.dp.toPx(),
                stop = size.width - 16.dp.toPx(),
                fraction = thumbPosition
            )

            drawCircle(
                color = thumbColor,
                radius = circleRadius.toPx(),
                center = Offset(x = thumbOffset, y = size.height / 2)
            )
        }
    }
}

private fun calculateThumbOffset(
    start: Float,
    stop: Float,
    fraction: Float
): Float = start + (stop - start) * fraction

@Preview
@Composable
private fun SwitchPreview() {
    SudokuTheme {
        Switch(
            checked = false,
            onCheckedChange = {}
        )
    }
}
