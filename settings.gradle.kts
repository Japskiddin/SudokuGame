enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Sudoku"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

include(
    ":app",
    ":core:navigation",
    ":core:database:data",
    ":core:database:entities",
    ":core:datastore",
    ":core:common",
    ":core:common-android",
    ":core:model",
    ":core:domain",
    ":core:game",
    ":core:designsystem",
    ":core:ui",
    ":core:feature",
    ":core:data",
    ":features:home:ui",
    ":features:home:ui-logic",
    ":features:home:domain",
    ":features:game:ui",
    ":features:game:ui-logic",
    ":features:game:domain",
    ":features:settings:ui",
    ":features:settings:ui-logic",
    ":features:settings:domain",
    ":features:history:ui",
    ":features:history:ui-logic",
    ":features:history:domain",
)
