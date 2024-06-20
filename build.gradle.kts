import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.bundletool)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.jetbrains.compose.compiler) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.detekt) apply false
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

allprojects.onEach { project ->
    project.afterEvaluate {
        with(project.plugins) {
            if (hasPlugin(libs.plugins.jetbrains.kotlin.android.get().pluginId) ||
                hasPlugin(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
            ) {
                apply(libs.plugins.detekt.get().pluginId)

                project.extensions.configure<DetektExtension> {
                    config.setFrom(rootProject.files("default-detekt-config.yml"))
                }

                project.dependencies.add("detektPlugins", libs.detekt.formatting.get().toString())
            }

            if (hasPlugin(libs.plugins.jetbrains.compose.compiler.get().pluginId)) {
                project.dependencies.add("detektPlugins", libs.detekt.rules.compose.get().toString())
            }
        }
    }
}
