package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureUiLogic() {
    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.jetbrains.kotlinx.coroutines.core)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
        implementation(libs.jetbrains.kotlinx.immutable)
        implementation(project(":core:common-android"))
        implementation(project(":core:navigation"))
        implementation(project(":core:model"))
        implementation(project(":core:feature"))
    }
}
