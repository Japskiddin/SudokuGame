import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidHilt
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            commonExtension.apply {
                configureAndroidHilt(this)
            }
        }
    }
}
