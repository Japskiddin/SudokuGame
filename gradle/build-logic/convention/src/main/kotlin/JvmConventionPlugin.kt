import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import io.github.japskiddin.android.core.buildlogic.projectJavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.kotlin.jvm.get().pluginId)
            }

            extensions.configure(KotlinProjectExtension::class.java) {
                explicitApi = ExplicitApiMode.Strict
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = projectJavaVersion
                targetCompatibility = projectJavaVersion
            }
        }
    }
}
