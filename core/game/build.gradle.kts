plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    applyDefaultHierarchyTemplate()
    android {
        namespace = "io.github.japskiddin.sudoku.core.game"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
        }
        val jvmAndroidMain = create("jvmAndroidMain") {
            dependsOn(commonMain.get())
        }
        androidMain.get().dependsOn(jvmAndroidMain)
        jvmMain.get().dependsOn(jvmAndroidMain)
    }
}
