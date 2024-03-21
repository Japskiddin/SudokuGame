package io.github.japskiddin.sudoku.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    .fillMaxWidth(.5f)
                    .weight(1f)
            )
            Text(text = viewModel.getCurrentYear())
        }
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.start_game),
            onClick = {}
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.settings),
            onClick = {}
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.records),
            onClick = {}
        )
    }
}

@Composable
fun MenuButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = modifier.size(36.dp),
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(4.dp)
                .weight(1f)
        )
    }
}

@Preview(
    name = "Menu Button"
)
@Composable
fun MenuButtonPreview() {
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
fun HomeScreenPreview() {
    HomeScreen()
}