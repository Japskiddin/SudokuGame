import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.app.feature.ui.logic)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.records.ui.logic"
}

dependencies {
    implementation(projects.features.records.domain)
}
