package io.github.japskiddin.sudoku.feature.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MenuScreen() {
    MenuScreen(viewModel = viewModel())
}

@Composable
internal fun MenuScreen(viewModel: MenuViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.title))
        Menu(modifier = Modifier.fillMaxWidth(.5f))
        Text(text = viewModel.getCurrentYear())
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.start_game)
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.settings)
        )
        MenuButton(
            icon = painterResource(id = R.drawable.ic_start_game),
            text = stringResource(id = R.string.records)
        )
    }
}

@Composable
fun MenuButton(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
    )
}

@Preview(
    name = "Menu Screen",
    showBackground = true
)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}