package io.github.japskiddin.sudoku.feature.home.ui

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
import androidx.compose.runtime.collectAsState
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
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonBackgroundNormal
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonBackgroundPressed
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonForegroundNormal
import io.github.japskiddin.sudoku.core.ui.theme.MenuButtonForegroundPressed
import io.github.japskiddin.sudoku.core.ui.theme.OnMenuButton
import io.github.japskiddin.sudoku.feature.home.domain.HomeViewModel
import io.github.japskiddin.sudoku.feature.home.domain.UiState

@Composable
public fun HomeScreen(modifier: Modifier = Modifier) {
  HomeScreen(modifier = modifier, viewModel = hiltViewModel())
}

@Composable
internal fun HomeScreen(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel
) {
  val state by viewModel.uiState.collectAsState()
  HomeScreenContent(
    modifier = modifier,
    state = state,
    currentYear = viewModel.currentYear,
    onStartGameClick = { viewModel.onStartClick() },
    onSettingsClick = { viewModel.onSettingsClick() },
    onRecordsClick = { viewModel.onRecordsClick() },
  )
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  state: UiState,
  currentYear: String,
  onStartGameClick: () -> Unit,
  onRecordsClick: () -> Unit,
  onSettingsClick: () -> Unit,
) {
  val screenModifier = Modifier
    .fillMaxSize()
    .then(modifier)
  when (state) {
    is UiState.Menu -> MainMenu(
      modifier = screenModifier,
      currentYear = currentYear,
      onStartGameClick = onStartGameClick,
      onRecordsClick = onRecordsClick,
      onSettingsClick = onSettingsClick,
    )

    is UiState.Loading -> Loading(
      modifier = screenModifier,
      resId = state.message,
    )
  }
}

@Composable
private fun MainMenu(
  modifier: Modifier = Modifier,
  currentYear: String,
  onStartGameClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onRecordsClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .then(modifier)
      .paint(
        painter = painterResource(id = R.drawable.home_background),
        contentScale = ContentScale.Crop
      )
      .padding(16.dp),
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Menu(
        modifier = Modifier
          .fillMaxWidth(.8f)
          .weight(1f),
        onStartGameClick = onStartGameClick,
        onSettingsClick = onSettingsClick,
        onRecordsClick = onRecordsClick,
      )
      OutlineText(
        text = currentYear,
        fillColor = Color.White,
        outlineColor = Color.Black
      )
    }
  }
}

@Composable
private fun Menu(
  modifier: Modifier = Modifier,
  onStartGameClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onRecordsClick: () -> Unit,
) {
  Column(
    modifier = modifier.wrapContentHeight(),
    verticalArrangement = Arrangement.Center,
  ) {
    OutlineText(
      text = stringResource(id = R.string.title),
      textSize = 48.sp,
      fillColor = Color.White,
      outlineColor = Color.Black,
      outlineWidth = 4.dp,
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
  onClick: () -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val buttonForegroundColor = if (isPressed) {
    MenuButtonForegroundPressed
  } else {
    MenuButtonForegroundNormal
  }
  val buttonBackgroundColor = if (isPressed) {
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
        bottomStroke = 6.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    MenuButtonContent(
      icon = icon,
      text = text,
      textColor = OnMenuButton,
      outlineColor = buttonBackgroundColor,
    )
  }
}

@Composable
private fun MenuButtonContent(
  modifier: Modifier = Modifier,
  icon: Painter,
  text: String,
  textColor: Color,
  outlineColor: Color,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(all = 12.dp)
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
private fun OutlineText(
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

private fun Modifier.drawBorder(
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
  name = "Main Menu",
  showBackground = true
)
@Composable
private fun MainMenuPreview() {
  MainMenu(
    currentYear = "2024",
    onStartGameClick = {},
    onRecordsClick = {},
    onSettingsClick = {},
  )
}

@Preview(
  name = "Menu Button"
)
@Composable
private fun MenuButtonPreview() {
  MenuButton(
    icon = painterResource(id = R.drawable.ic_start_game),
    text = stringResource(id = R.string.start_game),
    onClick = {},
  )
}