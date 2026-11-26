enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Sudoku"

pluginManagement {
    includeBuild("./gradle/build-logic")
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
    ":shared",
    ":androidApp",
    ":desktopApp",
    ":core:navigation",
    ":core:database:data",
    ":core:database:entities",
    ":core:datastore",
    ":core:common",
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
    ":features:about:ui",
    ":features:about:ui-logic",
    ":features:onboarding:ui",
    ":features:history:ui",
    ":features:history:ui-logic",
    ":features:history:domain",
)
