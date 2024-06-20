package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        val javaVersion = JavaVersion.toVersion(libs.findVersion("jvm").get())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
