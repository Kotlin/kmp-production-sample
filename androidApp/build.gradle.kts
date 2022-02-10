plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()

        applicationId = "com.github.jetbrains.rssreader.androidApp"
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            storeFile = file("./key/key.jks")
            com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).apply {
                storePassword = getProperty("storePwd")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPwd")
            }
        }
    }

    buildTypes {
        create("debugPG") {
            isDebuggable = false
            isMinifyEnabled = true
            versionNameSuffix = " debugPG"
            matchingFallbacks.add("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.get()
    }
    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        implementation(project(":shared"))
        //desugar utils
        coreLibraryDesugaring(libs.desugar.jdk.libs)
        //Compose
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.tooling)
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.compose.material)
        //Compose Utils
        implementation(libs.coil.compose)
        implementation(libs.activity.compose)
        implementation(libs.accompanist.insets)
        implementation(libs.accompanist.swiperefresh)
        //Coroutines
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)
        //DI
        implementation(libs.koin.core)
        implementation(libs.koin.android)
        //Navigation
        implementation(libs.voyager.navigator)
        //WorkManager
        implementation(libs.work.runtime.ktx)
    }
}
