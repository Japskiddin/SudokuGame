package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureJUnitAndroid() {
    dependencies {
        androidTestImplementation(libs.androidx.test.espresso.core)
        androidTestImplementation(libs.androidx.test.ext.junit)
        androidTestImplementation(libs.androidx.arch.core.testing)
        androidTestImplementation(libs.google.truth)

        if (pluginManager.hasPlugin(libs.plugins.jetbrains.compose.compiler.get().pluginId)) {
            androidTestImplementation(platform(libs.androidx.compose.bom))
            androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        }
    }
}

fun Project.configureJUnit() {
    dependencies {
        testImplementation(libs.junit)
    }
}
