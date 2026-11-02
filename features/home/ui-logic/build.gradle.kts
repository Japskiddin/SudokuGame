plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.feature.ui.logic)
    alias(libs.plugins.app.detekt)
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
