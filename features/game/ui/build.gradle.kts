import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.game.ui"

    kotlinOptions {
        allWarningsAsErrors = false
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}

dependencies {
    implementation(projects.features.game.uiLogic)
    implementation(projects.core.data)
}
