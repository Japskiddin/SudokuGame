import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureUiLogicConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.matching { it.name == "androidMain" }.configureEach {
                dependencies {
                    implementation(libs.androidx.core.ktx)
                }
            }
            sourceSets.named("commonMain") {
                dependencies {
                    implementation(libs.androidx.lifecycle.viewmodel)
                    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
                    implementation(libs.kotlinx.coroutines.core)
                    implementation(libs.metrox.viewmodel)
                    implementation(project(":core:common"))
                    implementation(project(":core:navigation"))
                    api(project(":core:model"))
                    api(project(":core:feature"))
                }
            }
        }
    }
}
