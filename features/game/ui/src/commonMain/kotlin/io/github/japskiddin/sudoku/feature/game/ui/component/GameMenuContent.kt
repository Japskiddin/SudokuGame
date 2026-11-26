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
import io.github.japskiddin.sudoku.core.ui.resources.ic_close
import io.github.japskiddin.sudoku.core.ui.resources.ic_continue
import io.github.japskiddin.sudoku.core.ui.resources.ic_settings
import io.github.japskiddin.sudoku.core.ui.resources.settings
import org.jetbrains.compose.resources.painterResource
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
            icon = painterResource(resource = Res.drawable.ic_continue),
            text = stringResource(resource = Res.string.continue_game),
            onClick = onContinue,
        )
        GameButton(
            icon = painterResource(resource = Res.drawable.ic_settings),
            text = stringResource(resource = Res.string.settings),
            onClick = onSettings,
        )
        GameButton(
            icon = painterResource(resource = Res.drawable.ic_close),
            text = stringResource(resource = Res.string.exit_game),
            onClick = onExit,
        )
    }
}
