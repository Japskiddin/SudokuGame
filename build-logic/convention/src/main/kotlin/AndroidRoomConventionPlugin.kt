import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidRoom
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            commonExtension.apply {
                configureAndroidRoom(this)
            }
        }
    }
}
