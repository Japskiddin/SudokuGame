package io.github.japskiddin.sudoku.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.settings),
            onClick = onSettingsClick,
//            modifier = Modifier.padding(top = 8.dp),
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.records),
            onClick = onRecordsClick,
//            modifier = Modifier.padding(top = 8.dp),
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

    val buttonContainerColor = Color(if (isPressed) 0xFFCC773C else 0xFFFAA468)
    val buttonShadowColor = Color(if (isPressed) 0xFFA05622 else 0xFFC6763F)
    val onButtonColor = Color.White

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = buttonContainerColor,
            containerColor = buttonShadowColor,
        ),
        contentPadding = PaddingValues(all = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .drawBorder(
                backgroundColor = buttonShadowColor,
                foregroundColor = buttonContainerColor,
                strokeWidth = 1.dp,
                cornerRadius = 8.dp,
                bottomStroke = 4.dp,
            )
    ) {
        MenuButtonContent(
            icon = icon,
            text = text,
            textColor = onButtonColor,
        )
    }

//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
////            .background(color = borderColor, shape = RoundedCornerShape(size = 8.dp))
////            .padding(start = 1.dp, top = 1.dp, end = 1.dp, bottom = 4.dp)
////            .background(color = containerColor, shape = RoundedCornerShape(size = 8.dp))
//            .drawBorder(
//                backgroundColor = buttonShadowColor,
//                foregroundColor = buttonContainerColor,
//                strokeWidth = 1.dp,
//                cornerRadius = 8.dp,
//                bottomStroke = 4.dp,
//            )
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center,
//    ) {
//        Image(
//            painter = icon,
//            contentDescription = text,
//            modifier = modifier.size(24.dp),
//        )
//        Text(
//            text = text,
//            textAlign = TextAlign.Center,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold,
//            color = onButtonColor,
//            modifier = modifier
//                .padding(start = 4.dp)
//                .weight(1f)
//        )
//    }
}

@Composable
internal fun MenuButtonContent(
    icon: Painter,
    text: String,
    textColor: Color,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .weight(1f)
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