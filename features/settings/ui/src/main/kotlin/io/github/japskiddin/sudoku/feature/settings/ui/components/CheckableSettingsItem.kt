package io.github.japskiddin.sudoku.feature.settings.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnPrimary
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchCheckedBorder
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchCheckedThumb
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchCheckedTrack
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchUncheckedBorder
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchUncheckedThumb
import io.github.japskiddin.sudoku.core.designsystem.theme.SettingsSwitchUncheckedTrack

private const val SwitchScale = 0.8f

@Composable
internal fun CheckableSettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = OnPrimary,
                style = MaterialTheme.typography.labelMedium
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    color = OnPrimary.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
                )
            }
        }
        Spacer(modifier.width(6.dp))
        Switch(
            modifier = Modifier.scale(SwitchScale),
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = SettingsSwitchUncheckedThumb,
                uncheckedTrackColor = SettingsSwitchUncheckedTrack,
                uncheckedBorderColor = SettingsSwitchUncheckedBorder,
                checkedThumbColor = SettingsSwitchCheckedThumb,
                checkedTrackColor = SettingsSwitchCheckedTrack,
                checkedBorderColor = SettingsSwitchCheckedBorder
            )
        )
    }
}

@Preview(
    name = "Checkable Settings item",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun CheckableSettingsItemPreview() {
    MaterialTheme {
        CheckableSettingsItem(
            title = "Title",
            description = "Description",
            onCheckedChange = {},
            checked = true
        )
    }
}
