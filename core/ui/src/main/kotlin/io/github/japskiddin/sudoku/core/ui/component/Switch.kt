package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Composable
public fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    checkedTrackColor: Color = SudokuTheme.colors.switchCheckedTrack,
    uncheckedTrackColor: Color = SudokuTheme.colors.switchUncheckedTrack,
    checkThumbColor: Color = SudokuTheme.colors.switchCheckedThumb,
    uncheckThumbColor: Color = SudokuTheme.colors.switchUncheckedThumb,
    borderColor: Color = SudokuTheme.colors.switchBorder,
    borderWidth: Dp = 4.dp,
    onCheckedChange: (Boolean) -> Unit,
) {
    val thumbPosition by animateFloatAsState(
        targetValue = if (checked) {
            1f
        } else {
            0f
        },
        label = "thumb position"
    )
    val thumbRadius = if (checked) {
        10.dp
    } else {
        8.dp
    }
    val interactionSource = remember { MutableInteractionSource() }
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

    Canvas(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .size(width = 56.dp, height = 32.dp)
            .clickable(
                onClick = { onCheckedChange(!checked) },
                interactionSource = interactionSource,
                indication = LocalIndication.current
            )
    ) {
        drawTrack(
            fillColor = trackColor,
            borderColor = borderColor,
            borderWidth = borderWidth
        )
        drawThumb(
            fillColor = thumbColor,
            borderColor = borderColor,
            borderWidth = borderWidth,
            radius = thumbRadius,
            position = thumbPosition,
            checked = checked
        )
    }
}

private fun DrawScope.drawThumb(
    fillColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    radius: Dp,
    position: Float,
    checked: Boolean,
) {
    val offset = calculateThumbOffset(
        start = 16.dp.toPx(),
        stop = size.width - 16.dp.toPx(),
        fraction = position
    )

    drawCircleWithStyle(
        color = fillColor,
        style = Fill,
        radius = radius,
        offset = offset
    )

    if (checked) {
        drawCircleWithStyle(
            color = borderColor,
            style = Stroke(
                width = borderWidth.toPx() - 2.dp.toPx()
            ),
            radius = radius,
            offset = offset
        )
    }
}

private fun DrawScope.drawCircleWithStyle(
    color: Color,
    style: DrawStyle,
    radius: Dp,
    offset: Float,
) {
    drawCircle(
        color = color,
        style = style,
        radius = radius.toPx(),
        center = Offset(
            x = offset,
            y = size.height / 2
        )
    )
}

private fun DrawScope.drawTrack(
    fillColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    cornerRadius: Dp = 16.dp
) {
    drawRoundRectWithStyle(
        color = fillColor,
        style = Fill,
        cornerRadius = cornerRadius
    )
    drawRoundRectWithStyle(
        color = borderColor,
        style = Stroke(
            width = borderWidth.toPx(),
        ),
        cornerRadius = cornerRadius
    )
}

private fun DrawScope.drawRoundRectWithStyle(
    color: Color,
    style: DrawStyle,
    cornerRadius: Dp,
) {
    val radiusPx = cornerRadius.toPx()

    drawRoundRect(
        style = style,
        color = color,
        size = Size(
            width = size.width,
            height = size.height
        ),
        cornerRadius = CornerRadius(
            x = radiusPx,
            y = radiusPx
        )
    )
}

private fun calculateThumbOffset(
    start: Float,
    stop: Float,
    fraction: Float
): Float = start + (stop - start) * fraction

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun SwitchPreview() {
    var checked by remember { mutableStateOf(true) }

    SudokuTheme {
        Row(modifier = Modifier.padding(12.dp)) {
            Switch(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}
