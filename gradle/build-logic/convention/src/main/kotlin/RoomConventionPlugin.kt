import io.github.japskiddin.android.core.buildlogic.configureRoom
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(libs.plugins.androidx.room.get().pluginId)
                apply(libs.plugins.google.devtools.ksp.get().pluginId)
            }

            configureRoom()
        }
    }
}
