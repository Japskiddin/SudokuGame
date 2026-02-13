import androidx.room.gradle.RoomExtension
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.ksp
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(libs.plugins.androidx.room.get().pluginId)
                apply(libs.plugins.google.devtools.ksp.get().pluginId)
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.room.ktx)
                ksp(libs.androidx.room.compiler)
            }
        }
    }
}
