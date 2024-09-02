package io.github.japskiddin.sudoku.feature.home.domain.usecase

import java.util.Calendar
import javax.inject.Inject

public class GetCurrentYearUseCase @Inject constructor() {
    public operator fun invoke(): String = Calendar.getInstance().get(Calendar.YEAR).toString()
}
