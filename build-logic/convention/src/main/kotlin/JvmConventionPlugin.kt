import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.configureJUnit
import io.github.japskiddin.android.core.buildlogic.configureKotlinJvm
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugin(Plugins.JetbrainsKotlinJvm))

            configureKotlinJvm()
            configureJUnit()
        }
    }
}
