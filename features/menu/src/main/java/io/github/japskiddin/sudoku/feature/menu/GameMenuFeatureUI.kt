package io.github.japskiddin.sudoku.feature.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MenuScreen() {
    Column {
        Text(text = stringResource(id = R.string.title))
    }
}

@Preview(
    name = "Menu Screen",
    showBackground = true
)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}