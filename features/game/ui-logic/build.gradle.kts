plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.feature.ui.logic)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.game.ui.logic"
}

dependencies {
    implementation(projects.features.game.domain)
}
