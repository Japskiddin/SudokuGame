plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.feature.domain)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.di)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.settings.domain"
    }
}
