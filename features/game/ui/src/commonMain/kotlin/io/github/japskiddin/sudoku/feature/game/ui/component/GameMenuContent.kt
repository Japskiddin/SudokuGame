package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.resources.Res
import io.github.japskiddin.sudoku.core.ui.resources.continue_game
import io.github.japskiddin.sudoku.core.ui.resources.exit_game
import io.github.japskiddin.sudoku.core.ui.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GameMenuContent(
    onContinue: () -> Unit,
    onSettings: () -> Unit,
    onExit: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GameButton(
            text = stringResource(resource = Res.string.continue_game),
            onClick = onContinue,
        )
        GameButton(
            text = stringResource(resource = Res.string.settings),
            onClick = onSettings,
        )
        GameButton(
            text = stringResource(resource = Res.string.exit_game),
            onClick = onExit,
        )
    }
}
