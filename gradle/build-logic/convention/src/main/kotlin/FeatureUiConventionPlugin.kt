import io.github.japskiddin.android.core.buildlogic.configureFeatureUi
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureFeatureUi()
        }
    }
}
