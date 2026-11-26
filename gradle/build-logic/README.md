# Convention plugins

This included build keeps shared Gradle configuration in Kotlin plugins. The root
[`settings.gradle.kts`](../../settings.gradle.kts) loads it with
`pluginManagement.includeBuild`. Plugin implementations and registrations live in
[`convention`](convention); their aliases and dependency versions come from the
shared [`libs.versions.toml`](../libs.versions.toml).

## Available plugins

| Plugin ID / catalog alias | Configuration |
| --- | --- |
| `app.multiplatform` / `libs.plugins.app.multiplatform` | Kotlin Multiplatform with Android library and JVM targets, strict explicit API, the shared JVM toolchain, Android SDK levels and Kotlin JVM target from the catalog. Each module supplies its Android namespace. |
| `app.compose` / `libs.plugins.app.compose` | Applies Compose Multiplatform and its compiler, configures stability and optional compiler metrics/reports. KMP Android libraries get Android resources, Compose dependencies and preview annotations in `commonMain`, and tooling on `androidRuntimeClasspath`. Android apps get Compose dependencies and debug tooling. |
| `app.android.application` / `libs.plugins.app.android.application` | Android application plugin, SDK levels and Java compatibility from the catalog, supported ABIs, BuildConfig/resource values, automatic locale configuration and packaging exclusions. Release builds use R8 and resource shrinking; debug builds append ` DEBUG` to the version name. |
| `app.jvm` / `libs.plugins.app.jvm` | Kotlin JVM plugin, JVM toolchain and Java compatibility from the catalog. Used by the desktop launcher; explicit API is not required. |
| `app.di` / `libs.plugins.app.di` | Metro compiler plugin. ViewModel integration dependencies are added by the feature conventions or explicitly by the application modules. |
| `app.room` / `libs.plugins.app.room` | Applies `app.multiplatform`, Room and KSP. Adds Room runtime to `commonMain`, bundled SQLite to `jvmMain`, Room processors to `kspAndroid`/`kspJvm`, and stores schemas in the module's `schemas/` directory. |
| `app.test` / `libs.plugins.app.test` | KMP: Kotlin Test and coroutines test in `commonTest`, JUnit in `jvmTest` and Android host tests; enables the Android host test compilation. Android application: JUnit/coroutines unit tests, AndroidJUnitRunner and instrumented test libraries. JVM: JUnit/coroutines unit tests. |
| `app.detekt` / `libs.plugins.app.detekt` | Detekt with the root configuration, ktlint and Compose Rules in modules that apply the Compose compiler. The `detekt` task scans Kotlin files in all source sets and writes Checkstyle XML, HTML, SARIF and Markdown reports. |
| `app.feature.domain` / `libs.plugins.app.feature.domain` | Applies `app.multiplatform`, `app.di`, `app.test` and `app.detekt`. Adds coroutines, `core:common`, and API dependencies on `core:domain` and `core:model`. |
| `app.feature.ui.logic` / `libs.plugins.app.feature.ui.logic` | Applies `app.multiplatform`, `app.di`, `app.test` and `app.detekt`. Adds ViewModel/saved state, coroutines, Metro ViewModels, `core:common`, `core:navigation`, and API dependencies on `core:model` and `core:feature`; Android also gets Core KTX. |
| `app.feature.ui` / `libs.plugins.app.feature.ui` | Applies `app.multiplatform`, `app.compose` and `app.detekt`. Adds lifecycle Compose/ViewModel and Metro ViewModel integration, `core:ui`, and Android Core KTX. |

Compose-specific Detekt rules also work when Compose is applied after `app.detekt`.

## Using the conventions

A feature layer needs only its own convention. For example, a UI module declares
its namespace, resource package and dependency on its sibling state module:

```kotlin
plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.home.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.home.uiLogic)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.feature.home.ui.resources"
}
```

Apply `app.compose` alongside the platform convention for a module that uses
Compose. For example, Compose core modules use `app.multiplatform` and
`app.compose`, and the Android app uses `app.android.application` and `app.compose`.

Keep module-specific dependencies, namespaces, resource packages, application
identity/version, signing, Tracer and desktop entry point settings in the module
scripts. Android device tests are opt-in: `core:database:data` declares its
`withDeviceTestBuilder` and device test dependencies locally.

## Compose compiler diagnostics

The Compose convention uses
[`config/compose_compiler_config.conf`](../../config/compose_compiler_config.conf).
Enable diagnostics for the desired build with Gradle properties:

```shell
./gradlew :androidApp:assembleDebug -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

Outputs go to the root build directory under each module path, for example
`build/androidApp/compose-metrics/`, `build/androidApp/compose-reports/` and
`build/features/home/ui/compose-reports/`.

## Maintaining build logic

Add shared behavior to the smallest applicable convention and keep versions in
the catalog. Register new plugin IDs in
[`convention/build.gradle.kts`](convention/build.gradle.kts) and add a matching
catalog alias. Build-logic dependencies belong to this included build rather
than a root `buildscript` block.

Validate the plugins, then exercise the affected consuming modules:

```shell
./gradlew -p gradle/build-logic :convention:check
./gradlew jvmTest :androidApp:testDebugUnitTest :androidApp:assembleDebug :desktopApp:compileKotlin
./gradlew detekt
```

See the [project README](../../README.md) for module responsibilities and setup.
