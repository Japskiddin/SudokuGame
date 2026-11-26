import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.github.japskiddin.android.core.buildlogic.debugImplementation
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.compose.multiplatform.get().pluginId)
        pluginManager.apply(libs.plugins.compose.compiler.get().pluginId)

        configureComposeCompiler()

        pluginManager.withPlugin(libs.plugins.android.multiplatform.library.get().pluginId) {
            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                    androidResources.enable = true
                }
                sourceSets.named("commonMain") {
                    dependencies {
                        api(libs.bundles.compose.multiplatform)
                        api(libs.compose.components.resources)
                        api(libs.androidx.lifecycle.runtime.compose)
                        implementation(libs.compose.ui.tooling.preview)
                    }
                }
            }
            dependencies.add("androidRuntimeClasspath", libs.compose.ui.tooling)
        }

        pluginManager.withPlugin(libs.plugins.android.application.get().pluginId) {
            extensions.configure<ApplicationExtension> {
                buildFeatures.compose = true
            }
            dependencies {
                implementation(libs.bundles.compose.multiplatform)
                debugImplementation(libs.compose.ui.tooling)
            }
        }
    }

    private fun Project.configureComposeCompiler() {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            val outputDirectory = rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
            metricsDestination.set(
                providers.gradleProperty("enableComposeCompilerMetrics")
                    .filter(String::toBoolean)
                    .flatMap { outputDirectory.map { it.dir("compose-metrics") } }
            )
            reportsDestination.set(
                providers.gradleProperty("enableComposeCompilerReports")
                    .filter(String::toBoolean)
                    .flatMap { outputDirectory.map { it.dir("compose-reports") } }
            )
            stabilityConfigurationFiles.add(
                rootProject.layout.projectDirectory.file("config/compose_compiler_config.conf")
            )
        }
    }
}
