package io.github.japskiddin.android.core.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidHilt(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    if (commonExtension is ApplicationExtension) {
        pluginManager.apply("com.google.dagger.hilt.android")
    }

    pluginManager.apply("com.google.devtools.ksp")

    dependencies {
        add("implementation", libs.findLibrary("dagger-hilt-android").get())
        add("ksp", libs.findLibrary("dagger-hilt-compiler").get())
    }
}
