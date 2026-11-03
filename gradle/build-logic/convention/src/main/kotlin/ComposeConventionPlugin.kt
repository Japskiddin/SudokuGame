import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.compose.compiler.get().pluginId)

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
