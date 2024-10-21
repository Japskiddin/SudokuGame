import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.core.feature"
}

dependencies {
    implementation(libs.androidx.annotation)

    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.common)
}
