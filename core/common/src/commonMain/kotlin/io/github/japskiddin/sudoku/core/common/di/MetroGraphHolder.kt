package io.github.japskiddin.sudoku.core.common.di

/**
 * Implemented by the Application so any module can retrieve the root Metro graph at runtime
 * (sync/work entry points cannot depend on :app where the concrete graph lives).
 */
public interface MetroGraphHolder {
    public val metroGraph: Any
}
