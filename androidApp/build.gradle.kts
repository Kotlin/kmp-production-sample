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
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    //UI
    implementation("androidx.appcompat:appcompat:${properties["version.androidx.appcompat"]}")
    implementation("com.google.android.material:material:${properties["version.androidx.material"]}")
    implementation("androidx.constraintlayout:constraintlayout:${properties["version.androidx.constraintlayout"]}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${properties["version.androidx.swiperefreshlayout"]}")
    implementation("androidx.core:core-ktx:${properties["version.androidx.ktx"]}")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${properties["version.kotlinx.coroutines"]}")
    //Log
    implementation("com.jakewharton.timber:timber:${properties["version.timber"]}")
    //DI
    implementation("org.koin:koin-core:${properties["version.koin"]}")
    implementation("org.koin:koin-androidx-scope:${properties["version.koin"]}")
    //Recycler utils
    implementation("com.mikepenz:fastadapter:${properties["version.fastadapter"]}")
    implementation("com.mikepenz:fastadapter-extensions-diff:${properties["version.fastadapter"]}")
    //Image load
    implementation("io.coil-kt:coil:${properties["version.coil"]}")
    //ViewBinding delegate
    implementation("com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:${properties["version.viewbindingpropertydelegate"]}")
}