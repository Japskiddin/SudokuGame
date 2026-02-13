import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import io.github.japskiddin.android.core.buildlogic.androidTestImplementation
import io.github.japskiddin.android.core.buildlogic.debugImplementation
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.compose.compiler.get().pluginId)
            }

            pluginManager.withPlugin(libs.plugins.android.application.get().pluginId) {
                extensions.getByType(ApplicationExtension::class).apply {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            pluginManager.withPlugin(libs.plugins.android.library.get().pluginId) {
                extensions.getByType(LibraryExtension::class).apply {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                fun Provider<String>.onlyIfTrue() = flatMap {
                    provider {
                        it.takeIf(String::toBoolean)
                    }
                }

                fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
                    rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
                }.map {
                    it.dir(dir)
                }

                project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
                    .relativeToRootProject("compose-metrics")
                    .let(metricsDestination::set)

                project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
                    .relativeToRootProject("compose-reports")
                    .let(reportsDestination::set)

                stabilityConfigurationFiles.addAll(
                    rootProject.layout.projectDirectory.file("config/compose_compiler_config.conf"),
                )
            }

            dependencies {
                val bom = platform(libs.androidx.compose.bom)
                implementation(bom)
                androidTestImplementation(bom)

                implementation(libs.bundles.compose)

                debugImplementation(libs.androidx.compose.ui.tooling)
                debugImplementation(libs.androidx.compose.ui.test.manifest)
            }
        }
    }
}
