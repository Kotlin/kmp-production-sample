rootProject.name = "RssReader"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        val kotlin_repo_url: String? by settings
        kotlin_repo_url?.let { maven(url = it) }
    }
}

dependencyResolutionManagement {
    versionCatalogs {

        create("libs") {

            val kotlin_version: String? by settings
            kotlin_version?.let { version("kotlin", it) }
        }
    }
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        val kotlin_repo_url: String? by settings
        kotlin_repo_url?.let { maven(url = it) }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":androidApp")
include(":desktopApp")
