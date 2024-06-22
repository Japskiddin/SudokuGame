plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "io.github.japskiddin.sudoku.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}
