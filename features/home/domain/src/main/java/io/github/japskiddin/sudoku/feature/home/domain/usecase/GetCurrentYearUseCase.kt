package io.github.japskiddin.sudoku.feature.home.domain.usecase

import java.util.Calendar
import javax.inject.Inject

internal class GetCurrentYearUseCase @Inject constructor() {
    operator fun invoke() = Calendar.getInstance().get(Calendar.YEAR).toString()
}
