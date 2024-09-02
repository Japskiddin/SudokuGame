package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureUiLogic() {
    dependencies {
        add("implementation", libs.findLibrary("androidx-core-ktx").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
        add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
        add("implementation", libs.findLibrary("jetbrains-kotlinx-coroutines-android").get())
        add("implementation", project(":core:common"))
        add("implementation", project(":core:navigation"))
        add("api", project(":core:data"))
    }
}
