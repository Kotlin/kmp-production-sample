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
        val kotlin_version: String? by settings
        val repoUrl = kotlin_repo_url
        val kotlinVersion = kotlin_version
        if (!repoUrl.isNullOrEmpty() && !kotlinVersion.isNullOrEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(url = repoUrl)
                }
                filter {
                    includeVersionByRegex("org\\.jetbrains\\.kotlin(\\..*)?", ".*", kotlinVersion)
                }
            }
        }
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
        val kotlin_version: String? by settings
        val repoUrl = kotlin_repo_url
        val kotlinVersion = kotlin_version
        if (!repoUrl.isNullOrEmpty() && !kotlinVersion.isNullOrEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(url = repoUrl)
                }
                filter {
                    includeVersionByRegex("org\\.jetbrains\\.kotlin(\\..*)?", ".*", kotlinVersion)
                }
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":androidApp")
include(":desktopApp")
