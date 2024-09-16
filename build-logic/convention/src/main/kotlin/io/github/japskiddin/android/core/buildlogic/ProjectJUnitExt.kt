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
            androidTestImplementation(libs.library(Libraries.AndroidXTestEspressoCore))
            androidTestImplementation(libs.library(Libraries.AndroidXTestExtJUnit))
            androidTestImplementation(libs.library(Libraries.AndroidXArchCoreTesting))
            androidTestImplementation(libs.library(Libraries.GoogleTruth))

            if (pluginManager.hasPlugin(libs.plugin(Plugins.JetbrainsComposeCompiler))) {
                androidTestImplementation(platform(libs.library(Libraries.AndroidXComposeBom)))
                androidTestImplementation(libs.library(Libraries.AndroidXComposeUiTestJUnit4))
            }
        }
    }
}

fun Project.configureJUnit() {
    dependencies {
        testImplementation(libs.library(Libraries.JUnit))
    }
}
