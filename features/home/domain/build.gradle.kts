import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.hilt)
    alias(libs.plugins.app.feature.domain)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.home.domain"
}

dependencies {
    api(libs.jetbrains.kotlinx.immutable)
    api(projects.core.game)
}
