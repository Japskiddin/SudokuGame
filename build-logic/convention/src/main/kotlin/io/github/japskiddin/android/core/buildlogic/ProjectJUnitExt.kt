package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureJUnit() {
    dependencies {
        add("testImplementation", libs.findLibrary("junit").get())
    }
}
