package io.github.japskiddin.sudoku.game

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.ok.tracer.heap.dumps.HeapDumpConfiguration

@HiltAndroidApp
class App : Application(), HasTracerConfiguration {
    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {},
            CrashReportConfiguration.build {},
            HeapDumpConfiguration.build {},
        )
}
