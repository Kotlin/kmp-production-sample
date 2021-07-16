import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

val composeVersion = findProperty("version.compose") as String

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
            gradleLocalProperties(rootDir).apply {
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
        viewBinding = true
        compose = true
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    //desugar utils
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    //Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    //Compose Utils
    implementation("com.google.accompanist:accompanist-coil:0.14.0")
    implementation("com.google.accompanist:accompanist-insets:0.14.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.14.0")
    //UI
    implementation("androidx.appcompat:appcompat:1.3.0")
    //Coroutines
    val coroutinesVersion = findProperty("version.kotlinx.coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    //DI
    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("io.insert-koin:koin-android:3.1.2")
    //Navigation
    implementation("com.github.terrakok:modo:0.6.1")
    implementation("com.github.terrakok:modo-render-android-fm:0.6.1")
    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.5.0")
}