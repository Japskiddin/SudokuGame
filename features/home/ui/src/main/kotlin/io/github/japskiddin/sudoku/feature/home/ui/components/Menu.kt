package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.feature.home.ui.R

@Composable
internal fun Menu(
    modifier: Modifier = Modifier,
    isShowContinueButton: Boolean,
    onStartGameClick: () -> Unit,
    onContinueGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecordsClick: () -> Unit
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlineText(
            text = stringResource(id = R.string.title),
            textStyle = MaterialTheme.typography.titleLarge,
            fillColor = Color.White,
            outlineColor = Color.Black,
            outlineWidth = 4.dp
        )
        if (isShowContinueButton) {
            GameButton(
                icon = painterResource(id = R.drawable.ic_start),
                text = stringResource(id = R.string.continue_game),
                onClick = onContinueGameClick,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }
        GameButton(
            icon = painterResource(id = R.drawable.ic_start),
            text = stringResource(id = R.string.start_game),
            onClick = onStartGameClick,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        GameButton(
            icon = painterResource(id = R.drawable.ic_settings),
            text = stringResource(id = R.string.settings),
            onClick = onSettingsClick,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        GameButton(
            icon = painterResource(id = R.drawable.ic_records),
            text = stringResource(id = R.string.records),
            onClick = onRecordsClick,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
