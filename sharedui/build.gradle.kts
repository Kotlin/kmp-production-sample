plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
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
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("version.kotlinx.coroutines")}")
            }
        }
        val androidMain by getting {
            dependencies {
                //Compose Utils
                implementation("io.coil-kt:coil-compose:1.4.0")
                implementation("com.google.accompanist:accompanist-insets:0.20.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-okhttp:${findProperty("version.ktor")}")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}