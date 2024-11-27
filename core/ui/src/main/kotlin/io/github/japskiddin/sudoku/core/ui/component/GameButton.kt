package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.R

@Composable
public fun GameButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconSize: Dp = 24.dp,
    cornerRadius: Dp = 8.dp,
    strokeWidth: Dp = 2.dp,
    bottomStroke: Dp = 6.dp,
    foregroundNormalColor: Color = SudokuTheme.colors.menuButtonForegroundNormal,
    foregroundPressedColor: Color = SudokuTheme.colors.menuButtonForegroundPressed,
    backgroundNormalColor: Color = SudokuTheme.colors.menuButtonBackgroundNormal,
    backgroundPressedColor: Color = SudokuTheme.colors.menuButtonBackgroundPressed,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonForegroundColor =
        if (isPressed) {
            foregroundPressedColor
        } else {
            foregroundNormalColor
        }
    val buttonBackgroundColor =
        if (isPressed) {
            backgroundPressedColor
        } else {
            backgroundNormalColor
        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = cornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .drawBorder(
                backgroundColor = buttonBackgroundColor,
                foregroundColor = buttonForegroundColor,
                strokeWidth = strokeWidth,
                cornerRadius = cornerRadius,
                bottomStroke = bottomStroke
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GameButtonContent(
            icon = icon,
            text = text,
            textColor = SudokuTheme.colors.onMenuButton,
            iconSize = iconSize,
            outlineColor = buttonBackgroundColor
        )
    }
}

@Composable
private fun GameButtonContent(
    text: String,
    icon: Painter?,
    iconSize: Dp,
    textColor: Color,
    outlineColor: Color,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 12.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.size(iconSize)
            )
        }
        OutlineText(
            text = text,
            textStyle = SudokuTheme.typography.labelMedium,
            fillColor = textColor,
            outlineColor = outlineColor,
            modifier = Modifier
                .padding(
                    start = contentPadding,
                    end = contentPadding
                )
                .weight(1f)
        )
    }
}

private fun Modifier.drawBorder(
    backgroundColor: Color,
    foregroundColor: Color,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 8.dp,
    bottomStroke: Dp = 4.dp,
    cornerRadiusMultiplier: Float = 1.2f,
) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val bottomStrokeWidthPx = bottomStroke.toPx()
        val cornerRadiusPx = cornerRadius.toPx()
        drawRoundRect(
            color = backgroundColor,
            cornerRadius = CornerRadius(
                cornerRadiusPx * cornerRadiusMultiplier,
                cornerRadiusPx * cornerRadiusMultiplier
            )
        )
        drawRoundRect(
            color = foregroundColor,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            topLeft = Offset(strokeWidthPx, strokeWidthPx),
            size = Size(size.width - strokeWidthPx * 2, size.height - bottomStrokeWidthPx)
        )
    }
)

@Preview(
    name = "Game Button",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun GameButtonPreview() {
    SudokuTheme {
        Column(modifier = Modifier.padding(12.dp)) {
            GameButton(
                text = stringResource(id = R.string.start_game),
                icon = painterResource(id = R.drawable.ic_close),
                onClick = {}
            )
        }
    }
}
