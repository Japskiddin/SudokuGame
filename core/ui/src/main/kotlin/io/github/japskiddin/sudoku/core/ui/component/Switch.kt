package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
    enabled: Boolean = true,
    borderWidth: Dp = 4.dp,
    colors: SwitchColors = SwitchDefaults.colors(),
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
    val thumbRadius by animateDpAsState(
        targetValue = if (checked) {
            8.dp
        } else {
            6.dp
        },
        label = "thumb radius"
    )
    val trackColor by colors.trackColor(enabled, checked)
    val thumbColor by colors.thumbColor(enabled, checked)
    val borderColor by colors.borderColor(enabled)
    val interactionSource = remember { MutableInteractionSource() }

    Canvas(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .size(width = 48.dp, height = 28.dp)
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

@Stable
public interface SwitchColors {
    @Composable
    public fun trackColor(enabled: Boolean, checked: Boolean): State<Color>

    @Composable
    public fun thumbColor(enabled: Boolean, checked: Boolean): State<Color>

    @Composable
    public fun borderColor(enabled: Boolean): State<Color>
}

private object SwitchDefaults {
    @Composable
    fun colors(
        checkedTrackColor: Color = SudokuTheme.colors.switchCheckedTrack,
        uncheckedTrackColor: Color = SudokuTheme.colors.switchUncheckedTrack,
        checkThumbColor: Color = SudokuTheme.colors.switchCheckedThumb,
        uncheckThumbColor: Color = SudokuTheme.colors.switchUncheckedThumb,
        borderColor: Color = SudokuTheme.colors.switchBorder,
    ): SwitchColors =
        DefaultSwitchColors(
            checkedTrackColor = checkedTrackColor,
            uncheckedTrackColor = uncheckedTrackColor,
            checkThumbColor = checkThumbColor,
            uncheckThumbColor = uncheckThumbColor,
            borderColor = borderColor,
        )
}

@Immutable
private class DefaultSwitchColors(
    private val checkedTrackColor: Color,
    private val uncheckedTrackColor: Color,
    private val checkThumbColor: Color,
    private val uncheckThumbColor: Color,
    private val borderColor: Color,
) : SwitchColors {
    @Composable
    override fun trackColor(enabled: Boolean, checked: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (enabled) {
                if (checked) {
                    checkedTrackColor
                } else {
                    uncheckedTrackColor
                }
            } else {
                if (checked) {
                    checkedTrackColor.copy(alpha = 0.8f)
                } else {
                    uncheckedTrackColor.copy(alpha = 0.8f)
                }
            },
            label = "track color"
        )
    }

    @Composable
    override fun thumbColor(enabled: Boolean, checked: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (enabled) {
                if (checked) {
                    checkThumbColor
                } else {
                    uncheckThumbColor
                }
            } else {
                if (checked) {
                    checkThumbColor.copy(alpha = 0.8f)
                } else {
                    uncheckThumbColor.copy(alpha = 0.8f)
                }
            },
            label = "thumb color"
        )
    }

    @Composable
    override fun borderColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                borderColor
            } else {
                borderColor.copy(alpha = 0.8f)
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultSwitchColors) return false

        if (checkedTrackColor != other.checkedTrackColor) return false
        if (uncheckedTrackColor != other.uncheckedTrackColor) return false
        if (checkThumbColor != other.checkThumbColor) return false
        if (uncheckThumbColor != other.uncheckThumbColor) return false
        if (borderColor != other.borderColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checkedTrackColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + checkThumbColor.hashCode()
        result = 31 * result + uncheckThumbColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        return result
    }
}

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
