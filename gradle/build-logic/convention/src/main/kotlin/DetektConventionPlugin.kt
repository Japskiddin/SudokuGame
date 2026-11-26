import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.extensions.FailOnSeverity
import io.github.japskiddin.android.core.buildlogic.detektPlugins
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.detekt.get().pluginId)
            }

            tasks.named<Detekt>("detekt") {
                setSource(fileTree("src") { include("**/*.kt") })
                reports {
                    checkstyle.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    markdown.required.set(true)
                }
            }

            extensions.configure<DetektExtension> {
                config.setFrom(rootProject.files("config/default-detekt-config.yml"))
                buildUponDefaultConfig.set(true)
                failOnSeverity.set(FailOnSeverity.Info)
            }

            dependencies {
                detektPlugins(libs.detekt.formatting)
            }
            pluginManager.withPlugin(libs.plugins.compose.compiler.get().pluginId) {
                extensions.configure<DetektExtension> {
                    config.from(rootProject.file("config/detekt-compose.yml"))
                }
                dependencies {
                    detektPlugins(libs.detekt.rules.compose)
                }
            }
        }
    }
}
