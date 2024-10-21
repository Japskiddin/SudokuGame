package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureUi() {
    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.dagger.hilt.navigation.compose)
        implementation(libs.jetbrains.kotlinx.immutable)
        implementation(project(":core:common-android"))
        implementation(project(":core:ui"))
        implementation(project(":core:model"))
    }
}
