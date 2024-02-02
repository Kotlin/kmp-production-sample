import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    //https://github.com/adrielcafe/voyager/issues/315
//    js {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain.dependencies {
            implementation(project(":shared"))
            //Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(libs.compose.imageloader)
            implementation(libs.voyager.navigator)
            //Datetime
            implementation(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            //Compose
            implementation(libs.activity.compose)
            implementation(compose.uiTooling)
            //WorkManager
            implementation(libs.work.runtime.ktx)
            //Coroutines
            implementation(libs.kotlinx.coroutines.android)
            //DI
            implementation(libs.koin.android)
        }

        jvmMain.dependencies {
            //Compose
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
        }
    }
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
}

compose {
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

//compose.experimental {
//    web.application {}
//}
