import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.multiplatform")
        pluginManager.apply("app.di")
        pluginManager.apply("app.test")
        pluginManager.apply("app.detekt")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonMain") {
                dependencies {
                    implementation(libs.kotlinx.coroutines.core)
                    api(project(":core:domain"))
                    implementation(project(":core:common"))
                    api(project(":core:model"))
                }
            }
        }
    }
}
