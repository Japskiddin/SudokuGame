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

enum class Libraries(val alias: String) {
    /* region App */

    AndroidXCoreKtx(alias = "androidx-core-ktx"),

    /* endregion */

    /* region Compose */

    AndroidXComposeBom(alias = "androidx-compose-bom"),
    AndroidXComposeFoundation(alias = "androidx-compose-foundation"),
    AndroidXComposeAnimation(alias = "androidx-compose-animation"),
    AndroidXComposeUi(alias = "androidx-compose-ui"),
    AndroidXComposeUiUnit(alias = "androidx-compose-ui-unit"),
    AndroidXComposeUiGraphics(alias = "androidx-compose-ui-graphics"),
    AndroidXComposeUiToolingPreview(alias = "androidx-compose-ui-tooling-preview"),
    AndroidXComposeMaterial3(alias = "androidx-compose-material3"),
    AndroidXComposeRuntime(alias = "androidx-compose-runtime"),

    /* region Compose Debug */

    AndroidXComposeUiTooling(alias = "androidx-compose-ui-tooling"),
    AndroidXComposeUiTestManifest(alias = "androidx-compose-ui-test-manifest"),

    /* endregion */

    /* region Compose Testing */

    AndroidXComposeUiTestJUnit4(alias = "androidx-compose-ui-test-junit4"),

    /* endregion */

    /* region Kotlin */

    JetbrainsKotlinXCoroutinesCore(alias = "jetbrains-kotlinx-coroutines-core"),
    JetbrainsKotlinXCoroutinesAndroid(alias = "jetbrains-kotlinx-coroutines-android"),

    /* endregion */

    /* endregion */

    /* region Lifecycle */
    AndroidXLifecycleRuntimeKtx(alias = "androidx-lifecycle-runtime-ktx"),
    AndroidXLifecycleRuntimeCompose(alias = "androidx-lifecycle-runtime-compose"),
    AndroidXLifecycleViewmodelCompose(alias = "androidx-lifecycle-viewmodel-compose"),
    AndroidXLifecycleViewmodelKtx(alias = "androidx-lifecycle-viewmodel-ktx"),

    /* endregion */

    /* region Room */

    AndroidXRoomRuntime(alias = "androidx-room-runtime"),
    AndroidXRoomCompiler(alias = "androidx-room-compiler"),
    AndroidXRoomKtx(alias = "androidx-room-ktx"),

    /* endregion */

    /* region DataStore */

    AndroidXDatastorePreferences(alias = "androidx-datastore-preferences"),
    AndroidXDatastore(alias = "androidx-datastore"),

    /* endregion */

    /* region DI Hilt */

    DaggerHiltAndroid(alias = "dagger-hilt-android"),
    DaggerHiltCompiler(alias = "dagger-hilt-compiler"),
    DaggerHiltNavigationCompose(alias = "dagger-hilt-navigation-compose"),
    JavaXInject(alias = "javax-inject"),

    /* endregion */

    /* region Testing */

    AndroidXTestEspressoCore(alias = "androidx-test-espresso-core"),
    AndroidXTestExtJUnit(alias = "androidx-test-ext-junit"),
    AndroidXArchCoreTesting(alias = "androidx-arch-core-testing"),
    JUnit(alias = "junit"),
    GoogleTruth(alias = "google-truth"),

    /* endregion */

    /* region Detekt */

    DetektFormatting(alias = "detekt-formatting"),
    DetektRulesCompose(alias = "detekt-rules-compose")

    /* endregion */
}

enum class Plugins(val alias: String) {
    AndroidApplication(alias = "android-application"),
    AndroidLibrary(alias = "android-library"),
    JetbrainsKotlinAndroid(alias = "jetbrains-kotlin-android"),
    JetbrainsKotlinJvm(alias = "jetbrains-kotlin-jvm"),
    JetbrainsComposeCompiler(alias = "jetbrains-compose-compiler"),
    GoogleDevtoolsKsp(alias = "google-devtools-ksp"),
    DaggerHiltAndroid(alias = "dagger-hilt-android"),
    AndroidXRoom(alias = "androidx-room"),
    Detekt(alias = "detekt")
}

enum class Versions(val alias: String) {
    Jvm(alias = "jvm"),
    AndroidSdkMin(alias = "androidSdk-min"),
    AndroidSdkCompile(alias = "androidSdk-compile"),
    AndroidSdkTarget(alias = "androidSdk-target")
}

enum class Projects(val path: String) {
    CoreDomain(path = ":core:domain"),
    CoreUi(path = ":core:ui"),
    CoreCommonAndroid(path = ":core:common-android"),
    CoreNavigation(path = ":core:navigation"),
    CoreModel(path = ":core:model"),
    CoreDatabaseEntities(path = ":core:database:entities")
}
