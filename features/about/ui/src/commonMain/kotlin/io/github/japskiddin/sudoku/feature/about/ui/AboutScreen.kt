package io.github.japskiddin.sudoku.feature.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.AppBar
import io.github.japskiddin.sudoku.core.ui.component.BackHandler
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.OutlineText
import io.github.japskiddin.sudoku.core.ui.resources.Res
import io.github.japskiddin.sudoku.core.ui.resources.about
import io.github.japskiddin.sudoku.core.ui.resources.about_description
import io.github.japskiddin.sudoku.core.ui.resources.about_rate_app
import io.github.japskiddin.sudoku.core.ui.resources.about_share_app
import io.github.japskiddin.sudoku.core.ui.resources.about_store_unavailable
import io.github.japskiddin.sudoku.core.ui.resources.about_version
import io.github.japskiddin.sudoku.core.ui.resources.app_name
import io.github.japskiddin.sudoku.core.ui.resources.logo
import io.github.japskiddin.sudoku.core.ui.utils.LocalAppVersion
import io.github.japskiddin.sudoku.feature.about.ui.logic.AboutViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
public fun AboutScreen(
    onRateApp: (() -> Boolean)?,
    onShareApp: (() -> Unit)?,
    modifier: Modifier = Modifier,
    viewModel: AboutViewModel = metroViewModel(),
) {
    BackHandler(onBack = viewModel::onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SudokuTheme.colors.primary)
            .safeDrawingPadding(),
    ) {
        AppBar(
            title = stringResource(Res.string.about),
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            onBack = viewModel::onBack,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppInfo()
                AboutActions(onRateApp = onRateApp, onShareApp = onShareApp)
            }
        }
        OutlineText(
            text = stringResource(Res.string.about_version, LocalAppVersion.current),
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            textStyle = SudokuTheme.typography.info,
        )
    }
}

@Composable
private fun AppInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SudokuTheme.colors.card, RoundedCornerShape(12.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(112.dp),
        )
        BasicText(
            text = stringResource(Res.string.app_name),
            style = SudokuTheme.typography.appBar.copy(
                color = SudokuTheme.colors.onCard,
                textAlign = TextAlign.Center,
            ),
        )
        BasicText(
            text = stringResource(Res.string.about_description),
            style = SudokuTheme.typography.dialog.copy(
                color = SudokuTheme.colors.onCard,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@Composable
private fun AboutActions(onRateApp: (() -> Boolean)?, onShareApp: (() -> Unit)?) {
    var storeUnavailable by rememberSaveable { mutableStateOf(false) }
    if (onRateApp != null) {
        GameButton(text = stringResource(Res.string.about_rate_app)) {
            storeUnavailable = !onRateApp()
        }
        if (storeUnavailable) {
            OutlineText(
                text = stringResource(Res.string.about_store_unavailable),
                textStyle = SudokuTheme.typography.info,
            )
        }
    }
    if (onShareApp != null) {
        GameButton(text = stringResource(Res.string.about_share_app), onClick = onShareApp)
    }
}
