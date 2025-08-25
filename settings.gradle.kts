enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "RssReader"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        val kotlin_repo_url: String? by settings
        kotlin_repo_url?.let { maven(url = it) }
    }
    plugins {
        kotlin("jvm") version "2.2.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        val kotlin_version: String by settings
        create("libs") {
            version("kotlin", kotlin_version)
        }
    }
    repositories {
        google()
        mavenCentral()
        val kotlin_repo_url: String? by settings
        kotlin_repo_url?.let { maven(url = it) }
    }
}

include(":composeApp")
include(":shared")