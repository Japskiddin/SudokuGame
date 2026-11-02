import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.compose.multiplatform.get().pluginId)
        pluginManager.apply(libs.plugins.compose.compiler.get().pluginId)
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonMain") {
                dependencies {
                    api(libs.bundles.compose.multiplatform)
                    api(libs.compose.components.resources)
                    api(libs.androidx.lifecycle.runtime.compose)
                }
            }
        }
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("config/compose_compiler_config.conf"))
        }
    }
}
