package io.github.japskiddin.sudoku.game

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import io.github.japskiddin.sudoku.core.common.di.MetroGraphHolder
import io.github.japskiddin.sudoku.game.di.AppGraph
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.ok.tracer.heap.dumps.HeapDumpConfiguration

class App : Application(), HasTracerConfiguration, MetroGraphHolder {
    val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override val metroGraph: Any get() = appGraph

    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {},
            CrashReportConfiguration.build {},
            HeapDumpConfiguration.build {},
        )
}
