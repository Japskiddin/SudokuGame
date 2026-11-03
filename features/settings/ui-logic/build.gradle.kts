plugins {
    alias(libs.plugins.app.feature.ui.logic)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.settings.ui.logic"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.settings.domain)
        }
    }
}
