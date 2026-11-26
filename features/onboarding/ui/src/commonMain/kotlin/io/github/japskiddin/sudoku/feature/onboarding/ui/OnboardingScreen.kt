package io.github.japskiddin.sudoku.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.resources.app_name
import io.github.japskiddin.sudoku.core.ui.utils.PreviewSudoku
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.Res
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_finish
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_next
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_notes
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_notes_description
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_notes_title
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_page
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_progress
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_progress_description
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_progress_title
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_rules
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_rules_description
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_rules_title
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_save_failed
import io.github.japskiddin.sudoku.feature.onboarding.ui.resources.onboarding_skip
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

private data class Slide(
    val illustration: DrawableResource,
    val title: StringResource,
    val description: StringResource,
)

private const val IllustrationAspectRatio = 5f / 3f
private val OnboardingBackground = Color(0xFFFFF6EB)

private val slides = listOf(
    Slide(Res.drawable.onboarding_rules, Res.string.onboarding_rules_title, Res.string.onboarding_rules_description),
    Slide(Res.drawable.onboarding_notes, Res.string.onboarding_notes_title, Res.string.onboarding_notes_description),
    Slide(
        Res.drawable.onboarding_progress,
        Res.string.onboarding_progress_title,
        Res.string.onboarding_progress_description,
    ),
)

@Composable
public fun OnboardingScreen(
    busy: Boolean,
    saveFailed: Boolean,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingSystemBars()
    val pager = rememberPagerState(pageCount = { slides.size })
    val scope = rememberCoroutineScope()
    val page = pager.currentPage
    val isLastPage = page == slides.lastIndex

    Column(
        modifier = modifier.fillMaxSize()
            .background(OnboardingBackground)
            .safeDrawingPadding()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingHeader(enabled = !busy, onSkip = onFinish)
        HorizontalPager(
            state = pager,
            modifier = Modifier.fillMaxWidth().weight(1f),
            userScrollEnabled = !busy,
        ) { index ->
            OnboardingSlide(slides[index])
        }
        OnboardingFooter(
            page = page,
            saveFailed = saveFailed,
            enabled = !busy && !pager.isScrollInProgress,
            action = if (isLastPage) Res.string.onboarding_finish else Res.string.onboarding_next,
            onAdvance = {
                if (isLastPage) onFinish() else scope.launch { pager.animateScrollToPage(page + 1) }
            },
        )
    }
}

@Composable
private fun OnboardingHeader(enabled: Boolean, onSkip: () -> Unit) {
    val brandColor = SudokuTheme.colors.gameButtonBackgroundNormal
    Row(
        modifier = Modifier.widthIn(max = 880.dp).fillMaxWidth().padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = stringResource(CoreUiRes.string.app_name),
            modifier = Modifier.weight(1f),
            style = SudokuTheme.typography.appBar.copy(color = brandColor),
        )
        Box(
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                .clickable(enabled = enabled, role = Role.Button, onClick = onSkip)
                .heightIn(min = 48.dp)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = stringResource(Res.string.onboarding_skip),
                style = SudokuTheme.typography.info.copy(color = brandColor),
            )
        }
    }
}

@Composable
private fun OnboardingSlide(slide: Slide) {
    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val viewportHeight = maxHeight
        if (maxWidth > viewportHeight) {
            Row(
                modifier = Modifier.widthIn(max = 832.dp).fillMaxWidth().verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    SlideIllustration(
                        slide = slide,
                        modifier = Modifier.heightIn(max = (viewportHeight - 24.dp).coerceAtLeast(0.dp)),
                    )
                }
                SlideText(slide, TextAlign.Start, Modifier.weight(1f), compact = viewportHeight < 280.dp)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                    .heightIn(min = viewportHeight)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SlideIllustration(slide, Modifier.widthIn(max = 420.dp).fillMaxWidth())
                SlideText(slide, TextAlign.Center, Modifier.widthIn(max = 420.dp).fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SlideIllustration(slide: Slide, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(slide.illustration),
        contentDescription = null,
        modifier = modifier.aspectRatio(IllustrationAspectRatio).clip(RoundedCornerShape(24.dp)),
    )
}

@Composable
private fun SlideText(slide: Slide, alignment: TextAlign, modifier: Modifier = Modifier, compact: Boolean = false) {
    val typography = SudokuTheme.typography
    val colors = SudokuTheme.colors
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        BasicText(
            text = stringResource(slide.title),
            modifier = Modifier.fillMaxWidth().semantics { heading() },
            style = typography.appBar.copy(
                color = colors.gameButtonBackgroundPressed,
                fontSize = if (compact) 22.sp else 32.sp,
                textAlign = alignment,
            ),
        )
        BasicText(
            text = stringResource(slide.description),
            modifier = Modifier.fillMaxWidth(),
            style = typography.dialog.copy(
                color = colors.gameButtonBackgroundPressed,
                lineHeight = 24.sp,
                textAlign = alignment,
            ),
        )
    }
}

@Composable
private fun OnboardingFooter(
    page: Int,
    saveFailed: Boolean,
    enabled: Boolean,
    action: StringResource,
    onAdvance: () -> Unit,
) {
    val color = SudokuTheme.colors.gameButtonBackgroundNormal
    val pageDescription = stringResource(Res.string.onboarding_page, page + 1, slides.size)

    Column(
        modifier = Modifier.widthIn(max = 468.dp).fillMaxWidth().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.clearAndSetSemantics { contentDescription = pageDescription },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(slides.size) { index ->
                val selected = index == page
                Box(
                    Modifier.width(if (selected) 24.dp else 8.dp).height(8.dp)
                        .background(if (selected) color else color.copy(alpha = 0.3f), CircleShape),
                )
            }
        }
        if (saveFailed) {
            BasicText(
                text = stringResource(Res.string.onboarding_save_failed),
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                style = SudokuTheme.typography.info.copy(color = color, textAlign = TextAlign.Center),
            )
        }
        GameButton(
            text = stringResource(action),
            enabled = enabled,
            onClick = onAdvance,
        )
    }
}

@PreviewSudoku
@Composable
private fun OnboardingPreview() {
    SudokuTheme {
        OnboardingScreen(busy = false, saveFailed = false, onFinish = {})
    }
}
