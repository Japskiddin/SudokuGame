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
        }
    }
}

fun Project.configureJUnit() {
    dependencies {
        add("testImplementation", libs.findLibrary("junit").get())
    }
}
