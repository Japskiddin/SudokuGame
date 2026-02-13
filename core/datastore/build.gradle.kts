plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}
