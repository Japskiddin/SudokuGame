import io.github.japskiddin.android.core.buildlogic.api

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.room)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.database"
}

dependencies {
    api(projects.core.database.entities)
    implementation(libs.androidx.annotation)
}
