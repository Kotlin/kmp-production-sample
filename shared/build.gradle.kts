import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "RssReader"
            isStatic = true
        }
    }

    jvm()

    compilerOptions{
        freeCompilerArgs.add("-Xpartial-linkage-loglevel=ERROR")
    }
    
    sourceSets {
        commonMain.dependencies {
            //Network
            implementation(libs.ktor.core)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.xml)
            //Coroutines
            implementation(libs.kotlinx.coroutines.core)
            //Logger
            implementation(libs.napier)
            //JSON
            implementation(libs.kotlinx.serialization.json)
            //Key-Value storage
            implementation(libs.multiplatform.settings)
            // DI
            api(libs.koin.core)

            //Date formatting
            implementation(libs.kotlinx.datetime)

            //XML
            implementation(libs.xml.serialization)
            implementation(libs.xml.serialization.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.github.jetbrains.rssreader"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    lint{
        abortOnError = false
        checkReleaseBuilds = false
    }
}