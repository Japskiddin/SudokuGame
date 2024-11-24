package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    androidExtension: AndroidExtension,
) {
    androidExtension.apply {
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
            val bom = platform(libs.androidx.compose.bom)
            implementation(bom)
            androidTestImplementation(bom)

            implementation(libs.androidx.compose.animation)
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.unit)
            implementation(libs.androidx.compose.ui.graphics)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.runtime)

            debugImplementation(libs.androidx.compose.ui.tooling)
            debugImplementation(libs.androidx.compose.ui.test.manifest)
        }
    }
}
