import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.application")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("io.github.skeptick.libres")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop")

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
                //Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.compose.libres)
                implementation(libs.compose.image.loader)
                //Datetime
                implementation(libs.kotlinx.datetime)
                implementation(libs.voyager.navigator)
            }
        }

        val androidMain by getting {
            dependencies {
                //Compose
                implementation(libs.activity.compose)
                implementation(libs.androidx.compose.ui.tooling)
                //WorkManager
                implementation(libs.work.runtime.ktx)
                //Coroutines
                implementation(libs.kotlinx.coroutines.android)
                //DI
                implementation(libs.koin.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                //Compose
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

libres {
    generatedClassName = "MR"
    generateNamedArguments = true
    baseLocaleLanguageCode = "en"
}

android {
    namespace = "com.github.jetbrains.rssreader"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()

        applicationId = "com.github.jetbrains.rssreader.androidApp"
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
}

tasks.getByPath("desktopProcessResources").dependsOn("libresGenerateResources")

compose {
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.20"))
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.21")

    desktop {
        application {
            mainClass = "MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "KotlinMultiplatformComposeDesktopApplication"
                packageVersion = "1.0.0"
            }
        }
    }
}
