import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidHilt
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            if (commonExtension is ApplicationExtension) {
                pluginManager.apply("com.google.dagger.hilt.android")
            }

            pluginManager.apply("com.google.devtools.ksp")

            configureAndroidHilt()
        }
    }
}
