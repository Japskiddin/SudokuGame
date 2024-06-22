package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDomain() {
    dependencies {
        add("implementation", libs.findLibrary("androidx-core-ktx").get())
        add("implementation", libs.findLibrary("javax-inject").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
        add("implementation", libs.findLibrary("jetbrains-kotlinx-coroutines-android").get())
        add("implementation", project(":core:common"))
        add("api", project(":core:data"))
        add("implementation", project(":core:navigation"))
    }
}
