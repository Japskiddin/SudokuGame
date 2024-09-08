import io.github.japskiddin.android.core.buildlogic.configureDetekt
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidDetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findLibrary("detekt-gradle-plugin").get().get().group.toString())
            configureDetekt()
        }
    }
}
