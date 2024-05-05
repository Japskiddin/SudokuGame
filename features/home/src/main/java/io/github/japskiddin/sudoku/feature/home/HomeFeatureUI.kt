package io.github.japskiddin.sudoku.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen() {
    HomeScreen(viewModel = hiltViewModel())
}

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.home_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.title))
            Menu(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .weight(1f),
                onStartGameClick = { viewModel.navigateToGame() },
                onSettingsClick = {},
                onRecordsClick = {},
            )
            Text(text = viewModel.getCurrentYear())
        }
    }
}

@Composable
internal fun Menu(
    modifier: Modifier = Modifier,
    onStartGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecordsClick: () -> Unit,
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.start_game),
            onClick = onStartGameClick,
            modifier = Modifier.padding(bottom = 8.dp)
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
internal fun MenuButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonForegroundColor = Color(if (isPressed) 0xFFCC773C else 0xFFFAA468)
    val buttonBackgroundColor = Color(if (isPressed) 0xFFA05622 else 0xFFC6763F)
    val onButtonColor = Color.White

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
                strokeWidth = 1.dp,
                cornerRadius = 8.dp,
                bottomStroke = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        MenuButtonContent(
            icon = icon,
            text = text,
            textColor = onButtonColor,
            outlineColor = buttonBackgroundColor,
        )
    }
}

@Composable
internal fun MenuButtonContent(
    icon: Painter,
    text: String,
    textColor: Color,
    outlineColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
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

@Composable
internal fun OutlineText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 16.sp,
    fillColor: Color,
    outlineColor: Color,
    outlineWidth: Dp = 2.dp,
) {
    Box {
        val fillTextStyle = TextStyle(
            color = fillColor,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        val outlineTextStyle = fillTextStyle.copy(
            color = outlineColor,
            drawStyle = Stroke(
                width = with(LocalDensity.current) { outlineWidth.toPx() },
                join = StrokeJoin.Round
            )
        )

        Text(
            text = text,
            style = LocalTextStyle.current.merge(outlineTextStyle),
            modifier = modifier.fillMaxWidth(),
        )
        Text(
            text = text,
            style = LocalTextStyle.current.merge(fillTextStyle),
            modifier = modifier.fillMaxWidth(),
        )
    }
}

internal fun Modifier.drawBorder(
    backgroundColor: Color,
    foregroundColor: Color,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 8.dp,
    bottomStroke: Dp = 4.dp
) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val bottomStrokeWidthPx = bottomStroke.toPx()
        val cornerRadiusPx = cornerRadius.toPx()
        drawRoundRect(
            color = backgroundColor,
            cornerRadius = CornerRadius(cornerRadiusPx * 1.2f, cornerRadiusPx * 1.2f)
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
internal fun MenuButtonPreview() {
    MenuButton(
        icon = painterResource(id = R.drawable.ic_start_game),
        text = stringResource(id = R.string.start_game),
        onClick = {},
    )
}

@Preview(
    name = "Home Screen",
    showBackground = true
)
@Composable
internal fun HomeScreenPreview() {
    HomeScreen()
}