package io.github.japskiddin.sudoku.core.ui.component

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
import io.github.japskiddin.sudoku.core.ui.utils.dialogBackground

@Composable
public fun Loading(
    modifier: Modifier = Modifier,
    text: String
) {
    LoadingContent(
        text = text,
        modifier = modifier
    )
}

@Composable
public fun Loading(
    modifier: Modifier = Modifier,
    @StringRes resId: Int
) {
    LoadingContent(
        text = stringResource(id = resId),
        modifier = modifier
    )
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
            .safeContentPadding()
    ) {
        val configuration = LocalConfiguration.current
        val widthPercent = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            .6f
        } else {
            1f
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(widthPercent)
                .dialogBackground()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                trackColor = Primary.copy(alpha = .2f),
                color = Primary,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

@Preview(
    name = "Loading - Portrait mode",
    device = Devices.PIXEL_2
)
@Preview(
    name = "Loading - Landscape mode",
    widthDp = 732,
    heightDp = 412
)
@Composable
private fun LoadingPreview() {
    Loading(
        text = "Please, wait..."
    )
}
