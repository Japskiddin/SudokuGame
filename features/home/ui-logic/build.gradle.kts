plugins {
    alias(libs.plugins.app.feature.ui.logic)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.home.ui.logic"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.home.domain)
        }
    }
}
