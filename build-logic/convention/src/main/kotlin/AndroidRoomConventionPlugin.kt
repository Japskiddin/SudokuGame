import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidRoom
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }

            commonExtension.apply {
                configureAndroidRoom(this)
            }
        }
    }
}
