import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project(":shared"))
            //Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            //Compose Utils
            implementation(libs.coil.compose)
            implementation(libs.activity.compose)
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
            //Splash
            implementation(libs.androidx.core.splashscreen)
        }
    }
}

android {
    namespace = "com.github.jetbrains.rssreader"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.jetbrains.rssreader"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.1"
    }

    signingConfigs {
        create("release") {
            storeFile = file("./key/key.jks")
            gradleLocalProperties(rootDir, providers).apply {
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
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
