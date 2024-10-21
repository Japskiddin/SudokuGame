package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDomain() {
    dependencies {
        implementation(libs.javax.inject)
        implementation(libs.jetbrains.kotlinx.coroutines.core)
        implementation(project(":core:domain"))
        implementation(project(":core:common"))
        implementation(project(":core:model"))
    }
}
