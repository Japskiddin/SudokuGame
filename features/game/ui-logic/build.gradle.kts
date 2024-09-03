import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.hilt)
    alias(libs.plugins.app.feature.ui.logic)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.game.ui.logic"
}

dependencies {
    api(libs.jetbrains.kotlinx.immutable)
    implementation(projects.features.game.domain)
    api(projects.core.game) // TODO implementation instead / remove from preview
}
