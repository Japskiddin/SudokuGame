import io.github.japskiddin.android.core.buildlogic.api
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.javax.inject)
                implementation(libs.jetbrains.kotlinx.coroutines.core)
                api(project(":core:domain"))
                implementation(project(":core:common"))
                api(project(":core:model"))
            }
        }
    }
}
