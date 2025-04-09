import io.github.japskiddin.android.core.buildlogic.androidConfig
import io.github.japskiddin.android.core.buildlogic.configureJUnit
import io.github.japskiddin.android.core.buildlogic.configureJUnitAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            androidConfig {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                configureJUnitAndroid()
            }

            configureJUnit()
        }
    }
}
