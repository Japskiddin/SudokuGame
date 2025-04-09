import io.github.japskiddin.android.core.buildlogic.configureDetekt
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.detekt.get().pluginId)
            }

            configureDetekt()
        }
    }
}
