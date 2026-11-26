plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.compose)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.test)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.app"
    }
    sourceSets {
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
        androidMain.dependencies {
            implementation(libs.androidx.navigation.compose)
        }
        commonMain.dependencies {
            api(projects.core.database.data)
            api(projects.core.datastore)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.data)
            implementation(projects.core.navigation)
            implementation(libs.navigation.compose.multiplatform)
            api(libs.metrox.viewmodel)
            implementation(libs.metrox.viewmodel.compose)
            implementation(projects.features.home.ui)
            implementation(projects.features.home.uiLogic)
            implementation(projects.features.home.domain)
            implementation(projects.features.game.ui)
            implementation(projects.features.game.uiLogic)
            implementation(projects.features.game.domain)
            implementation(projects.features.settings.ui)
            implementation(projects.features.settings.uiLogic)
            implementation(projects.features.settings.domain)
            implementation(projects.features.history.ui)
            implementation(projects.features.history.uiLogic)
            implementation(projects.features.history.domain)
        }
    }
}
