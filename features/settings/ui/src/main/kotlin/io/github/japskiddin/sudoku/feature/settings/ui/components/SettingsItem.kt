package io.github.japskiddin.sudoku.feature.settings.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    isCheckable: Boolean = true
) {
    Row(
        modifier = Modifier.padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title)
            if (description != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = description)
            }
        }
        if (isCheckable) {
            Spacer(modifier.width(6.dp))
        }
    }
}

@Preview(
    name = "Settings item",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun SettingsItemPreview() {
    MaterialTheme {
        SettingsItem(
            title = "Title",
            description = "Description"
        )
    }
}
