plugins {
    alias(libs.plugins.app.feature.domain)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.home.domain"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.game)
        }
    }
}
