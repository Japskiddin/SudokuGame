import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidRoom
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(libs.plugin(Plugins.AndroidXRoom))
                apply(libs.plugin(Plugins.GoogleDevtoolsKsp))
            }

            commonExtension.apply {
                configureAndroidRoom(this)
            }
        }
    }
}
