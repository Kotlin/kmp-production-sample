import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}


kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "RssReader"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-core:${properties["version.ktor"]}")
                implementation("io.ktor:ktor-client-logging:${properties["version.ktor"]}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
                //Logger
                implementation("com.github.aakira:napier:1.4.1")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${properties["version.kotlinx.serialization"]}")
                //Key-Value storage
                implementation("com.russhwolf:multiplatform-settings:0.6.2")
            }
        }

        val mobileMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(mobileMain)
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-okhttp:${properties["version.ktor"]}")
            }
        }

        val iosMain by getting {
            dependsOn(mobileMain)
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-ios:${properties["version.ktor"]}")
            }
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
        }
    }
}

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())

    defaultConfig {
        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)