package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.ui.R
import io.github.japskiddin.sudoku.core.ui.theme.OnPrimary
import io.github.japskiddin.sudoku.core.ui.theme.Primary

@Composable
fun Loading(
  modifier: Modifier = Modifier,
  text: String,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .then(modifier)
      .background(Primary),
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      CircularProgressIndicator(
        trackColor = OnPrimary.copy(alpha = 0.2f),
        color = OnPrimary,
        strokeWidth = 6.dp,
        strokeCap = StrokeCap.Round,
        modifier = Modifier.size(48.dp)
      )
      Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = OnPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp)
      )
    }
  }
}

@Preview(
  name = "Loading"
)
@Composable
internal fun LoadingPreview() {
  Loading(
    text = stringResource(R.string.please_wait)
  )
}