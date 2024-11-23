package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import io.github.japskiddin.sudoku.core.designsystem.theme.MenuButtonBackgroundNormal
import io.github.japskiddin.sudoku.core.designsystem.theme.MenuButtonBackgroundPressed
import io.github.japskiddin.sudoku.core.designsystem.theme.MenuButtonForegroundNormal
import io.github.japskiddin.sudoku.core.designsystem.theme.MenuButtonForegroundPressed
import io.github.japskiddin.sudoku.core.designsystem.theme.OnMenuButton
import io.github.japskiddin.sudoku.core.ui.R

@Composable
public fun GameButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconSize: Dp = 24.dp,
    foregroundNormalColor: Color = MenuButtonForegroundNormal,
    foregroundPressedColor: Color = MenuButtonForegroundPressed,
    backgroundNormalColor: Color = MenuButtonBackgroundNormal,
    backgroundPressedColor: Color = MenuButtonBackgroundPressed,
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
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .drawBorder(
                backgroundColor = buttonBackgroundColor,
                foregroundColor = buttonForegroundColor,
                strokeWidth = 2.dp,
                cornerRadius = 8.dp,
                bottomStroke = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GameButtonContent(
            icon = icon,
            text = text,
            textColor = OnMenuButton,
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
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 6.dp
            ),
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
            textStyle = MaterialTheme.typography.labelMedium,
            fillColor = textColor,
            outlineColor = outlineColor,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
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
)
@Composable
private fun GameButtonPreview() {
    GameButton(
        text = stringResource(id = R.string.start_game),
        icon = painterResource(id = R.drawable.ic_close),
        onClick = {}
    )
}
