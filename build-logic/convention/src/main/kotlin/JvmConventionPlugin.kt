import io.github.japskiddin.android.core.buildlogic.configureJvm
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
                apply(libs.plugins.dependency.analysis.get().pluginId)
            }

            configureJvm()
        }
    }
}
