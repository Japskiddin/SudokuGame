import io.github.japskiddin.android.core.buildlogic.configureFeatureDomain
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureFeatureDomain()
        }
    }
}
