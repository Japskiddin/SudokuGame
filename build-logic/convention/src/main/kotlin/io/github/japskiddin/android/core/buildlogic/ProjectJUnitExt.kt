package io.github.japskiddin.android.core.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureJUnitAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        dependencies {
            add("androidTestImplementation", libs.findLibrary("androidx-test-espresso-core").get())
            add("androidTestImplementation", libs.findLibrary("androidx-test-ext-junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-arch-core-testing").get())
            add("androidTestImplementation", libs.findLibrary("google-truth").get())

            if (pluginManager.hasPlugin(libs.findPlugin("jetbrains-compose-compiler").get().get().pluginId)) {
                add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
            }
        }
    }
}

fun Project.configureJUnit() {
    dependencies {
        add("testImplementation", libs.findLibrary("junit").get())
    }
}
