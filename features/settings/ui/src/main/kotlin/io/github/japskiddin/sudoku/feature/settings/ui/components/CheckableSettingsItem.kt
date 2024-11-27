package io.github.japskiddin.sudoku.feature.settings.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.Switch

@Composable
internal fun CheckableSettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            BasicText(
                text = title,
                style = SudokuTheme.typography.labelMedium.copy(
                    color = SudokuTheme.colors.onPrimary
                )
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = description,
                    style = SudokuTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                        color = SudokuTheme.colors.onPrimary.copy(alpha = 0.8f)
                    )
                )
            }
        }
        Spacer(modifier.width(6.dp))
        Switch(checked = checked) { checked ->
            onCheckedChange(checked)
        }
    }
}

@Preview(
    name = "Checkable Settings item",
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Composable
private fun CheckableSettingsItemPreview() {
    SudokuTheme {
        CheckableSettingsItem(
            title = "Title",
            description = "Description",
            onCheckedChange = {},
            checked = true
        )
    }
}
