package io.github.japskiddin.sudoku.feature.home.ui.components

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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonBackgroundNormal
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonBackgroundPressed
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonForegroundNormal
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonForegroundPressed
import io.github.japskiddin.sudoku.core.ui.theme.OnMenuButton
import io.github.japskiddin.sudoku.feature.home.ui.R

@Composable
internal fun Menu(
    modifier: Modifier = Modifier,
    onStartGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecordsClick: () -> Unit
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlineText(
            text = stringResource(id = R.string.title),
            textSize = 48.sp,
            fillColor = Color.White,
            outlineColor = Color.Black,
            outlineWidth = 4.dp
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.start_game),
            onClick = onStartGameClick,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.settings),
            onClick = onSettingsClick,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.records),
            onClick = onRecordsClick,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun MenuButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonForegroundColor =
        if (isPressed) {
            MenuButtonForegroundPressed
        } else {
            MenuButtonForegroundNormal
        }
    val buttonBackgroundColor =
        if (isPressed) {
            MenuButtonBackgroundPressed
        } else {
            MenuButtonBackgroundNormal
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
        MenuButtonContent(
            icon = icon,
            text = text,
            textColor = OnMenuButton,
            outlineColor = buttonBackgroundColor
        )
    }
}

@Composable
private fun MenuButtonContent(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    textColor: Color,
    outlineColor: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        OutlineText(
            text = text,
            fillColor = textColor,
            outlineColor = outlineColor,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
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
    cornerRadiusMultiplier: Float = 1.2f
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
    name = "Menu Button"
)
@Composable
private fun MenuButtonPreview() {
    MenuButton(
        icon = painterResource(id = R.drawable.ic_start_game),
        text = stringResource(id = R.string.start_game),
        onClick = {}
    )
}
