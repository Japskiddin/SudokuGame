plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.room)
}

android {
    namespace = "io.github.japskiddin.sudoku.database"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
