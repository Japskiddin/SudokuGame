import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidCompose
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugin(Plugins.JetbrainsComposeCompiler))

            commonExtension.apply {
                configureAndroidCompose(this)
            }
        }
    }
}
