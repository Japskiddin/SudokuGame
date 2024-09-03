import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.compose)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.core.ui"
}

dependencies {
    api(projects.core.designsystem)
}
