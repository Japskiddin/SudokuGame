package io.github.japskiddin.android.core.buildlogic

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.the

fun Project.plugins(block: PluginManager.() -> Unit) {
    with(pluginManager) {
        block()
    }
}

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

val Project.projectJavaVersion: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.jvm.get().toInt())
