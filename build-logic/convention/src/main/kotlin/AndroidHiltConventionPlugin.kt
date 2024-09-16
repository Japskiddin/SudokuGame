import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.commonExtension
import io.github.japskiddin.android.core.buildlogic.configureAndroidHilt
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            if (commonExtension is ApplicationExtension) {
                pluginManager.apply(libs.plugin(Plugins.DaggerHiltAndroid))
            }

            pluginManager.apply(libs.plugin(Plugins.GoogleDevtoolsKsp))

            configureAndroidHilt()
        }
    }
}
