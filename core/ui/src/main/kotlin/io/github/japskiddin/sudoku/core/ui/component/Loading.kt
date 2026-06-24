package io.github.japskiddin.sudoku.core.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.utils.SudokuPreview
import io.github.japskiddin.sudoku.core.ui.utils.panelBackground

@Composable
public fun Loading(
    text: String,
    modifier: Modifier = Modifier,
) {
    LoadingContent(
        text = text,
        modifier = modifier
    )
}

@Composable
public fun Loading(
    @StringRes resId: Int,
    modifier: Modifier = Modifier,
) {
    LoadingContent(
        text = stringResource(id = resId),
        modifier = modifier
    )
}

@Composable
private fun LoadingContent(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(SudokuTheme.colors.primary)
            .safeContentPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.panelBackground()
        ) {
            BasicText(
                text = text,
                style = SudokuTheme.typography.panel.copy(
                    color = SudokuTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                trackColor = SudokuTheme.colors.primary.copy(alpha = .2f),
                indicatorColor = SudokuTheme.colors.primary,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@SudokuPreview
@Composable
private fun LoadingPreview() {
    SudokuTheme {
        Loading(
            text = "Please, wait..."
        )
    }
}
