package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureUiLogic() {
    dependencies {
        implementation(libs.library(Libraries.AndroidXCoreKtx))
        implementation(libs.library(Libraries.AndroidXLifecycleRuntimeKtx))
        implementation(libs.library(Libraries.AndroidXLifecycleViewmodelCompose))
        implementation(libs.library(Libraries.AndroidXLifecycleViewmodelKtx))
        implementation(libs.library(Libraries.JetbrainsKotlinXCoroutinesCore))
        implementation(libs.library(Libraries.JetbrainsKotlinXCoroutinesAndroid))
        implementation(findProject(Projects.CoreCommonAndroid))
        implementation(findProject(Projects.CoreNavigation))
        api(findProject(Projects.CoreModel))
    }
}
