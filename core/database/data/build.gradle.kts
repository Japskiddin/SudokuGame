import io.github.japskiddin.android.core.buildlogic.api
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.room)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.database"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(projects.core.database.entities)
}
