import io.github.japskiddin.android.core.buildlogic.configureFeatureUiLogic
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureUiLogicConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureFeatureUiLogic()
        }
    }
}
