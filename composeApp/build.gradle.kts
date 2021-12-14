import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.compose")
}

repositories {
    mavenLocal() //for voyager desktop
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
                //Compose UI
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.uiTooling)
                //Navigation
                implementation("cafe.adriel.voyager:voyager-core:2.0.0")
                implementation("cafe.adriel.voyager:voyager-navigator:2.0.0")
                //DI
                implementation("io.insert-koin:koin-core:3.1.4")
                //Network
                implementation("io.ktor:ktor-client-okhttp:${findProperty("version.ktor")}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("version.kotlinx.coroutines")}")
            }
        }
        val androidMain by getting {
            dependencies {
                //Compose Utils
                implementation("io.coil-kt:coil-compose:1.4.0")
                implementation("androidx.activity:activity-compose:1.4.0")
                implementation("com.google.accompanist:accompanist-insets:0.20.0")
                implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${findProperty("version.kotlinx.coroutines")}")
                //DI
                implementation("io.insert-koin:koin-android:3.1.4")
                //WorkManager
                implementation("androidx.work:work-runtime-ktx:2.7.1")
            }
        }
        val jvmMain by getting {
            dependencies {
                //Compose UI
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

//android app
android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    }
}


// desktop app
// ./gradlew :composeApp:run
compose.desktop {
    application {
        mainClass = "com.github.jetbrains.rssreader.desktopApp.AppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "RSS reader"
            packageVersion = "1.0"
            macOS {
                bundleID = "com.github.jetbrains.rssreader.desktopApp"
                signing {
                    sign.set(false)
                }
            }
        }
    }
}