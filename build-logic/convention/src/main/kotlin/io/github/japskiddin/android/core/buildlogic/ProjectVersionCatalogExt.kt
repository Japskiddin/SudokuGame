package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.library(library: Libraries): Provider<MinimalExternalModuleDependency> =
    findLibrary(library.alias).get()

fun VersionCatalog.plugin(plugin: Plugins): String = findPlugin(plugin.alias).get().get().pluginId

fun VersionCatalog.version(version: Versions): VersionConstraint = findVersion(version.alias).get()

fun Project.findProject(project: Projects): Project = project(project.path)

sealed class Libraries(val alias: String) {
    /* region App */

    object AndroidXCoreKtx : Libraries(alias = "androidx-core-ktx")

    /* endregion */

    /* region Compose */

    object AndroidXComposeBom : Libraries(alias = "androidx-compose-bom")
    object AndroidXComposeFoundation : Libraries(alias = "androidx-compose-foundation")
    object AndroidXComposeAnimation : Libraries(alias = "androidx-compose-animation")
    object AndroidXComposeUi : Libraries(alias = "androidx-compose-ui")
    object AndroidXComposeUiUnit : Libraries(alias = "androidx-compose-ui-unit")
    object AndroidXComposeUiGraphics : Libraries(alias = "androidx-compose-ui-graphics")
    object AndroidXComposeUiToolingPreview : Libraries(alias = "androidx-compose-ui-tooling-preview")
    object AndroidXComposeMaterial3 : Libraries(alias = "androidx-compose-material3")
    object AndroidXComposeRuntime : Libraries(alias = "androidx-compose-runtime")

    /* region Compose Debug */

    object AndroidXComposeUiTooling : Libraries(alias = "androidx-compose-ui-tooling")
    object AndroidXComposeUiTestManifest : Libraries(alias = "androidx-compose-ui-test-manifest")

    /* endregion */

    /* region Compose Testing */

    object AndroidXComposeUiTestJUnit4 : Libraries(alias = "androidx-compose-ui-test-junit4")

    /* endregion */

    /* region Kotlin */

    object JetbrainsKotlinXCoroutinesCore : Libraries(alias = "jetbrains-kotlinx-coroutines-core")
    object JetbrainsKotlinXCoroutinesAndroid : Libraries(alias = "jetbrains-kotlinx-coroutines-android")

    /* endregion */

    /* endregion */

    /* region Lifecycle */
    object AndroidXLifecycleRuntimeKtx : Libraries(alias = "androidx-lifecycle-runtime-ktx")
    object AndroidXLifecycleRuntimeCompose : Libraries(alias = "androidx-lifecycle-runtime-compose")
    object AndroidXLifecycleViewmodelCompose : Libraries(alias = "androidx-lifecycle-viewmodel-compose")
    object AndroidXLifecycleViewmodelKtx : Libraries(alias = "androidx-lifecycle-viewmodel-ktx")

    /* endregion */

    /* region Room */

    object AndroidXRoomRuntime : Libraries(alias = "androidx-room-runtime")
    object AndroidXRoomCompiler : Libraries(alias = "androidx-room-compiler")
    object AndroidXRoomKtx : Libraries(alias = "androidx-room-ktx")

    /* endregion */

    /* region DataStore */

    object AndroidXDatastorePreferences : Libraries(alias = "androidx-datastore-preferences")
    object AndroidXDatastore : Libraries(alias = "androidx-datastore")

    /* endregion */

    /* region DI Hilt */

    object DaggerHiltAndroid : Libraries(alias = "dagger-hilt-android")
    object DaggerHiltCompiler : Libraries(alias = "dagger-hilt-compiler")
    object DaggerHiltNavigationCompose : Libraries(alias = "dagger-hilt-navigation-compose")
    object JavaXInject : Libraries(alias = "javax-inject")

    /* endregion */

    /* region Testing */

    object AndroidXTestEspressoCore : Libraries(alias = "androidx-test-espresso-core")
    object AndroidXTestExtJUnit : Libraries(alias = "androidx-test-ext-junit")
    object AndroidXArchCoreTesting : Libraries(alias = "androidx-arch-core-testing")
    object JUnit : Libraries(alias = "junit")
    object GoogleTruth : Libraries(alias = "google-truth")

    /* endregion */

    /* region Detekt */

    object DetektFormatting : Libraries(alias = "detekt-formatting")
    object DetektRulesCompose : Libraries(alias = "detekt-rules-compose")

    /* endregion */
}

sealed class Plugins(val alias: String) {
    object AndroidApplication : Plugins(alias = "android-application")
    object AndroidLibrary : Plugins(alias = "android-library")
    object JetbrainsKotlinAndroid : Plugins(alias = "jetbrains-kotlin-android")
    object JetbrainsKotlinJvm : Plugins(alias = "jetbrains-kotlin-jvm")
    object JetbrainsComposeCompiler : Plugins(alias = "jetbrains-compose-compiler")
    object GoogleDevtoolsKsp : Plugins(alias = "google-devtools-ksp")
    object DaggerHiltAndroid : Plugins(alias = "dagger-hilt-android")
    object AndroidXRoom : Plugins(alias = "androidx-room")
    object Detekt : Plugins(alias = "detekt")
}

sealed class Versions(val alias: String) {
    object Jvm : Versions(alias = "jvm")
    object AndroidSdkMin : Versions(alias = "androidSdk-min")
    object AndroidSdkCompile : Versions(alias = "androidSdk-compile")
    object AndroidSdkTarget : Versions(alias = "androidSdk-target")
}

sealed class Projects(val path: String) {
    object CoreDomain : Projects(path = ":core:domain")
    object CoreUi : Projects(path = ":core:ui")
    object CoreCommonAndroid : Projects(path = ":core:common-android")
    object CoreNavigation : Projects(path = ":core:navigation")
    object CoreModel : Projects(path = ":core:model")
    object CoreDatabaseEntities : Projects(path = ":core:database:entities")
}
