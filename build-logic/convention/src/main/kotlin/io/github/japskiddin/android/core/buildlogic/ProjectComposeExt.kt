package io.github.japskiddin.android.core.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        extensions.configure<ComposeCompilerGradlePluginExtension> {
            fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
            fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
                rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
            }.map { it.dir(dir) }

            project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
                .relativeToRootProject("compose-metrics")
                .let(metricsDestination::set)

            project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
                .relativeToRootProject("compose-reports")
                .let(reportsDestination::set)

            stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
        }

        dependencies {
            val bom = platform(libs.library(Libraries.AndroidXComposeBom))
            implementation(bom)
            androidTestImplementation(bom)

            implementation(libs.library(Libraries.AndroidXComposeAnimation))
            implementation(libs.library(Libraries.AndroidXComposeFoundation))
            implementation(libs.library(Libraries.AndroidXComposeUi))
            implementation(libs.library(Libraries.AndroidXComposeUiUnit))
            implementation(libs.library(Libraries.AndroidXComposeUiGraphics))
            implementation(libs.library(Libraries.AndroidXComposeUiToolingPreview))
            implementation(libs.library(Libraries.AndroidXComposeMaterial3))
            implementation(libs.library(Libraries.AndroidXComposeRuntime))

            debugImplementation(libs.library(Libraries.AndroidXComposeUiTooling))
            debugImplementation(libs.library(Libraries.AndroidXComposeUiTestManifest))
        }
    }
}
