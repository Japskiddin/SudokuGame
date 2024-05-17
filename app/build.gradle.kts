import java.io.FileInputStream
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.google.ksp)
  alias(libs.plugins.hilt)
}

kotlin {
  jvmToolchain(17)
}

android {
  namespace = "io.github.japskiddin.sudoku.game"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "io.github.japskiddin.sudoku.game"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    setProperty("archivesBaseName", "sudoku-${versionName}-${versionCode}")
  }

  val secretProperties = Properties()
  if (project.hasProperty("Keys.repo")) {
    val keyRepo = project.property("Keys.repo") as String
    val projectPropsFile = file("$keyRepo/google-play-publish.properties")
    if (projectPropsFile.exists()) {
      val props = Properties()
      props.load(FileInputStream(projectPropsFile))
      secretProperties.setProperty(
        "SIGNING_KEYSTORE_PATH", file(keyRepo + props["RELEASE_STORE_FILE"]).path
      )
      secretProperties.setProperty(
        "SIGNING_KEYSTORE_PASSWORD", props["RELEASE_STORE_PASS"].toString()
      )
      secretProperties.setProperty("SIGNING_KEY_ALIAS", props["RELEASE_KEY_ALIAS"].toString())
      secretProperties.setProperty("SIGNING_KEY_PASSWORD", props["RELEASE_KEY_PASS"].toString())
    } else {
      secretProperties.setProperty(
        "SIGNING_KEYSTORE_PATH", file("../android-signing-keystore.jks").path
      )
      secretProperties.setProperty(
        "SIGNING_KEYSTORE_PASSWORD", System.getenv("SIGNING_KEYSTORE_PASSWORD")
      )
      secretProperties.setProperty("SIGNING_KEY_ALIAS", System.getenv("SIGNING_KEY_ALIAS"))
      secretProperties.setProperty("SIGNING_KEY_PASSWORD", System.getenv("SIGNING_KEY_PASSWORD"))
    }
  } else {
    secretProperties.setProperty(
      "SIGNING_KEYSTORE_PATH", file("../android-signing-keystore.jks").path
    )
    secretProperties.setProperty(
      "SIGNING_KEYSTORE_PASSWORD", System.getenv("SIGNING_KEYSTORE_PASSWORD")
    )
    secretProperties.setProperty("SIGNING_KEY_ALIAS", System.getenv("SIGNING_KEY_ALIAS"))
    secretProperties.setProperty("SIGNING_KEY_PASSWORD", System.getenv("SIGNING_KEY_PASSWORD"))
  }
  val releaseSigning = signingConfigs.create("release") {
    storeFile = file(secretProperties["SIGNING_KEYSTORE_PATH"] as String)
    storePassword = secretProperties["SIGNING_KEYSTORE_PASSWORD"] as String
    keyAlias = secretProperties["SIGNING_KEY_ALIAS"] as String
    keyPassword = secretProperties["SIGNING_KEY_PASSWORD"] as String
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      signingConfig = releaseSigning
    }
    debug {
      versionNameSuffix = "-DEBUG"
      signingConfig = releaseSigning
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  bundle {
    language {
      enableSplit = false
    }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
  }

  kotlinOptions {
    allWarningsAsErrors = false
    freeCompilerArgs += listOf(
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )
  }

  lint {
    abortOnError = false
  }

  dependenciesInfo {
    includeInApk = false
    includeInBundle = false
  }

  packaging {
    jniLibs {
      excludes += listOf(
        "**/kotlin/**",
        "META-INF/androidx.*",
        "META-INF/proguard/androidx-*"
      )
    }
    resources {
      excludes += listOf(
        "/META-INF/*.kotlin_module",
        "**/kotlin/**",
        "**/*.txt",
        "**/*.xml",
        "**/*.properties",
        "META-INF/DEPENDENCIES",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/license.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "META-INF/notice.txt",
        "META-INF/ASL2.0",
        "META-INF/*.version",
        "META-INF/androidx.*",
        "META-INF/proguard/androidx-*"
      )
    }
  }

  applicationVariants.all {
    val variant = this
    variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
      .forEach { output ->
        val outputFileName = "sudoku-${variant.versionName}-${buildType.name}.apk"
        output.outputFileName = outputFileName
      }
  }
}

tasks.withType<JavaCompile> {
  val compilerArgs = options.compilerArgs
  compilerArgs.addAll(
    listOf(
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    )
  )
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  implementation(libs.jetbrains.kotlin.stdlib)
  implementation(libs.jetbrains.kotlinx.coroutines.android)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.activity.compose)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.runtime.livedata)

  implementation(libs.hilt.navigation.compose)
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)

  implementation(project(":features:home"))
  implementation(project(":features:game"))
  implementation(project(":game-data"))
  implementation(project(":core:common"))
  implementation(project(":datastore"))
  implementation(project(":database"))
  implementation(project(":navigation"))

  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
}