import androidx.room.gradle.RoomExtension
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("app.multiplatform")
                apply(libs.plugins.androidx.room.get().pluginId)
                apply(libs.plugins.ksp.get().pluginId)
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.named("commonMain") {
                    dependencies {
                        implementation(libs.androidx.room.runtime)
                    }
                }
                sourceSets.named("jvmMain") {
                    dependencies {
                        implementation(libs.androidx.sqlite.bundled)
                    }
                }
            }

            dependencies {
                add("kspAndroid", libs.androidx.room.compiler)
                add("kspJvm", libs.androidx.room.compiler)
            }
        }
    }
}
