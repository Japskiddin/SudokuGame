plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.di)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.common.android"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    api(projects.core.common)
}
