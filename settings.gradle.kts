rootProject.name = "RssReader"

include(":shared")
include(":composeApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        val kotlin = "1.8.10"
        kotlin("android").version(kotlin)
        kotlin("multiplatform").version(kotlin)
        kotlin("plugin.serialization").version(kotlin)
        kotlin("native.cocoapods").version(kotlin)

        val agp = "7.4.2"
        id("com.android.application").version(agp)
        id("com.android.library").version(agp)

        id("com.github.ben-manes.versions").version("0.46.0")
        id("org.jetbrains.compose").version("1.4.0-alpha01-dev975")
        id("io.github.skeptick.libres").version("1.1.5")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    versionCatalogs {
        create("libs") {
            //Compose
            library("androidx-compose-ui-tooling", "androidx.compose.ui:ui-tooling:1.3.3")
            library("activity-compose", "androidx.activity:activity-compose:1.6.1")
            library("compose-libres", "io.github.skeptick.libres:libres-compose:1.1.5")
            library("compose-image-loader", "io.github.qdsfdhvh:image-loader:1.2.10")
            library("voyager-navigator", "ca.gosyer:voyager-navigator:1.0.0-rc07")

            val ktor = "2.2.4"
            library("ktor-core", "io.ktor:ktor-client-core:$ktor")
            library("ktor-logging", "io.ktor:ktor-client-logging:$ktor")
            library("ktor-client-okhttp", "io.ktor:ktor-client-okhttp:$ktor")
            library("ktor-client-ios", "io.ktor:ktor-client-darwin:$ktor")
            library("ktor-client-js", "io.ktor:ktor-client-js:$ktor")

            val kotlinxCoroutines = "1.6.4"
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutines")
            library("kotlinx-coroutines-android", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxCoroutines")

            library("xml-serialization", "io.github.pdvrieze.xmlutil:serialization:0.85.0")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            library("kotlinx-datetime", "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            library("napier", "io.github.aakira:napier:2.6.1")
            library("multiplatform-settings", "com.russhwolf:multiplatform-settings:1.0.0")

            //DI
            val koin = "3.3.3"
            library("koin-core", "io.insert-koin:koin-core:$koin")
            library("koin-android", "io.insert-koin:koin-android:$koin")

            //Android
            library("work-runtime-ktx", "androidx.work:work-runtime-ktx:2.8.0")
            library("desugar-jdk-libs", "com.android.tools:desugar_jdk_libs:2.0.2")
        }
    }
}