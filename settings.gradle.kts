enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
        mavenCentral()
        gradlePluginPortal()
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

rootProject.name = "Sudoku"
include(
    ":app",
    ":core:navigation",
    ":core:database",
    ":core:datastore",
    ":core:common",
    ":core:game",
    ":core:designsystem",
    ":core:ui",
    ":core:data",
    ":features:home:ui",
    ":features:home:domain",
    ":features:game:ui",
    ":features:game:ui-logic",
    ":features:game:domain"
)
