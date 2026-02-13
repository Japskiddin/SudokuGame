plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.common.android"
}

dependencies {
    api(projects.core.common)
}
