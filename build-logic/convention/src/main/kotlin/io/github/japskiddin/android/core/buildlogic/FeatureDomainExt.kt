package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDomain() {
    dependencies {
        add("implementation", libs.findLibrary("androidx-core-ktx").get())
        add("implementation", libs.findLibrary("javax-inject").get())
        add("implementation", libs.findLibrary("jetbrains-kotlinx-coroutines-core").get())
        add("api", project(":core:data"))
    }
}
