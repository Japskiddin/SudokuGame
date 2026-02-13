plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.feature"
}

dependencies {
    implementation(libs.androidx.annotation)

    implementation(projects.core.ui)
    api(projects.core.model)
    implementation(projects.core.common)
}
