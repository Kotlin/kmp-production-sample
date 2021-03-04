plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())

    defaultConfig {
        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
        buildToolsVersion = properties["android.buildToolsVersion"] as String

        applicationId = "com.github.jetbrains.rssreader.androidApp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        create("debugPG") {
            initWith(getByName("debug"))
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
            isMinifyEnabled = false
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
        kotlinCompilerExtensionVersion = "1.0.0-beta01"
    }
}

dependencies {
    implementation(project(":shared"))
    //desugar utils
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    //Compose
    implementation("androidx.compose.ui:ui:1.0.0-beta01")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta01")
    implementation("androidx.compose.foundation:foundation:1.0.0-beta01")
    implementation("androidx.compose.material:material:1.0.0-beta01")
    //Compose Utils
    implementation("dev.chrisbanes.accompanist:accompanist-coil:0.6.0")
    implementation("dev.chrisbanes.accompanist:accompanist-insets:0.6.0")
    //UI
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.3.2")
    //Coroutines
    val coroutinesVersion = properties["version.kotlinx.coroutines"]
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    //Log
    implementation("com.jakewharton.timber:timber:4.7.1")
    //DI
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.koin:koin-androidx-scope:2.2.2")
    //Adapter
    implementation("com.mikepenz:fastadapter:5.3.2")
    implementation("com.mikepenz:fastadapter-extensions-diff:5.2.3")
    //Image load
    implementation("io.coil-kt:coil:1.0.0")
    //Navigation
    implementation("com.github.terrakok:cicerone:6.5")
    //ViewBinding delegate
    implementation("com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:1.4.1")
    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.5.0")
}