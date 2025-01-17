import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.data"
}

dependencies {
    implementation(libs.jetbrains.kotlinx.coroutines.core)

    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.database.data)
    implementation(projects.core.database.entities)
    implementation(projects.core.datastore)

    api(libs.javax.inject)
}
