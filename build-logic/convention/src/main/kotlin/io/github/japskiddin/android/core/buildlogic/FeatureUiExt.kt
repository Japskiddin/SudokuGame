package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureUi() {
    dependencies {
        implementation(libs.library(Libraries.AndroidXCoreKtx))
        implementation(libs.library(Libraries.AndroidXLifecycleRuntimeKtx))
        implementation(libs.library(Libraries.AndroidXLifecycleViewmodelCompose))
        implementation(libs.library(Libraries.AndroidXLifecycleViewmodelKtx))
        implementation(libs.library(Libraries.AndroidXLifecycleRuntimeCompose))
        implementation(libs.library(Libraries.DaggerHiltNavigationCompose))
        implementation(findProject(Projects.CoreUi))
    }
}
