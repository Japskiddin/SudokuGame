import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.database.data)
    implementation(projects.core.datastore)

    implementation(libs.javax.inject)
}
