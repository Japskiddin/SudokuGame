package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidHilt() {
    dependencies {
        implementation(libs.library(Libraries.DaggerHiltAndroid))
        ksp(libs.library(Libraries.DaggerHiltCompiler))
    }
}
