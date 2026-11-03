plugins {
    alias(libs.plugins.app.feature.domain)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.game.domain"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.game)
        }
    }
}
